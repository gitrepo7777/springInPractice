/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.service.impl;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.springinpractice.ch08.dao.SubscriberDao;
import com.springinpractice.ch08.model.Subscriber;
import com.springinpractice.ch08.service.ConfirmationExpiredException;
import com.springinpractice.ch08.service.ConfirmationFailedException;
import com.springinpractice.ch08.service.MailingListService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
@Transactional(
	propagation = Propagation.REQUIRED,
	isolation = Isolation.DEFAULT,
	readOnly = true)
public class MailingListServiceImpl implements MailingListService {
	private static final String SUBSCRIBE_TEMPLATE_PATH = "mailingListSubscribe.vm";
	private static final String UNSUBSCRIBE_TEMPLATE_PATH = "mailingListUnsubscribe.vm";
	private static final long ONE_DAY_IN_MS = 24 * 60 * 60 * 1000;
	private static final Logger log = LoggerFactory.getLogger(MailingListServiceImpl.class);
	
	@Inject private SubscriberDao subscriberDao;
	@Inject private JavaMailSender mailSender;
	@Inject private VelocityEngine velocityEngine;
	
	@Value("#{mailingListServiceProps.noReplyEmailAddress}")
	private String noReplyEmailAddress;
	
	@Value("#{mailingListServiceProps.confirmSubscriptionUrl}")
	private String confirmSubscriptionUrl;
	
	@Value("#{mailingListServiceProps.confirmUnsubscriptionUrl}")
	private String confirmUnsubscriptionUrl;
	
	@Value("#{mailingListServiceProps.confirmationKey}")
	private String confirmationKey;
	
	
	// =================================================================================================================
	// Subscription service methods
	// =================================================================================================================
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.service.MailingListService#getSubscriber(java.lang.Long)
	 */
	@Override
	public Subscriber getSubscriber(Long id) {
		isTrue(id > 0L);
		return subscriberDao.load(id);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.service.MailingListService#addSubscriber
	 * (com.springinpractice.ch08.model.Subscriber)
	 */
	@Override
	@Async
	@Transactional(readOnly = false)
	public void addSubscriber(Subscriber subscriber) {
		notNull(subscriber, "subscriber required");
		isTrue(!subscriber.isConfirmed(), "subscriber must be unconfirmed");
		notNull(subscriber.getIpAddress(), "subscriber.ipAddress required");
		notNull(subscriber.getDateCreated(), "subscriber.dateCreated required");
		
		log.info("Adding unconfirmed subscriber: " + subscriber);
		
		// If the subscriber already exists, then this method throws an exception. The user never sees this because
		// we're running this method asynchronously. That's what we want.
		subscriberDao.create(subscriber);
		
		sendConfirmSubscriptionEmail(subscriber);
	}
	
	private void sendConfirmSubscriptionEmail(Subscriber subscriber) {
		isTrue(subscriber != null);
		log.info("Sending confirm subscription e-mail to: " + subscriber);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		String digest = generateSubscriptionDigest(subscriber);
		String url = confirmSubscriptionUrl + "?s=" + subscriber.getId() + "&amp;d=" + digest;
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("subscriber", subscriber);
		model.put("url", url);
		
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, SUBSCRIBE_TEMPLATE_PATH, model);
		
		try {
			helper.setSubject("Please confirm your subscription");
			helper.setTo(subscriber.getEmail());
			helper.setFrom(noReplyEmailAddress);
			helper.setSentDate(subscriber.getDateCreated());
			helper.setText(text, true);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
		mailSender.send(message);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.service.MailingListService#confirmSubscriber(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public void confirmSubscriber(Long subscriberId, String digest) throws ConfirmationFailedException {
		isTrue(subscriberId > 0L);
		notNull(digest);
		
		Subscriber subscriber = getSubscriber(subscriberId);
		
		// Check timestamp first to avoid SHA-based DoS.
		checkTimestamp(subscriber.getDateCreated().getTime());
		
		// Now generate the digest and check for a match.
		String expectedDigest = generateSubscriptionDigest(subscriber);
		if (!digest.equals(expectedDigest)) {
			throw new ConfirmationFailedException("Bad digest");
		}
		
		// All good, confirm it
		log.info("Confirming subscriber: " + subscriber);
		subscriber.setConfirmed(true);
		subscriberDao.update(subscriber);
	}
	
	private String generateSubscriptionDigest(Subscriber subscriber) {
		return DigestUtils.shaHex(subscriber.getId() + ":" + confirmationKey);
	}
	
	
	// =================================================================================================================
	// Unsubscription service methods
	// =================================================================================================================
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.service.MailingListService#requestUnsubscription(java.lang.String)
	 */
	@Override
	@Async
	@Transactional(readOnly = false)
	public void requestUnsubscription(String email) {
		notNull(email);
		sendConfirmUnsubscriptionEmail(email);
	}
	
	private void sendConfirmUnsubscriptionEmail(String email) {
		isTrue(email != null);
		log.info("Sending confirm unsubscription e-mail to: " + email);
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		Date now = new Date();
		long timestamp = now.getTime();
		
		String digest = generateUnsubscriptionDigest(email, timestamp);
		String url = confirmUnsubscriptionUrl + "?e=" + email + "&amp;t=" + timestamp + "&amp;d=" + digest;
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("url", url);
		
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, UNSUBSCRIBE_TEMPLATE_PATH, model);
		
		try {
			helper.setSubject("Please confirm your unsubscription");
			helper.setTo(email);
			helper.setFrom(noReplyEmailAddress);
			helper.setSentDate(now);
			helper.setText(text, true);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
		mailSender.send(message);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.service.MailingListService#confirmUnsubscription(java.lang.String, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public void confirmUnsubscription(String email, Long timestamp, String digest) throws ConfirmationFailedException {
		notNull(email, "email can't be null");
		notNull(digest, "digest required");
		
		// Check timestamp first to avoid SHA-based DoS.
		checkTimestamp(timestamp);
		
		// Now generate the digest and check for a match.
		String expectedDigest = generateUnsubscriptionDigest(email, timestamp);
		if (!digest.equals(expectedDigest)) {
			throw new ConfirmationFailedException("Bad digest");
		}
		
		// All good, confirm it
		log.info("Confirming unsubscription for: " + email);
		subscriberDao.deleteByEmail(email);
	}
	
	private String generateUnsubscriptionDigest(String email, long timestamp) {
		return DigestUtils.shaHex(email + ":" + timestamp + ":" + confirmationKey);
	}
	
	
	// =================================================================================================================
	// Helper methods
	// =================================================================================================================
	
	private static void checkTimestamp(long timestamp) throws ConfirmationExpiredException {
		long now = System.currentTimeMillis();
		if (now - timestamp > ONE_DAY_IN_MS) {
			throw new ConfirmationExpiredException();
		}
	}
}
