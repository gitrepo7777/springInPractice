/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.web;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springinpractice.ch08.model.Subscriber;
import com.springinpractice.ch08.model.Unsubscriber;
import com.springinpractice.ch08.service.ConfirmationExpiredException;
import com.springinpractice.ch08.service.ConfirmationFailedException;
import com.springinpractice.ch08.service.MailingListService;

/**
 * <p>
 * Subscription flow looks like this:
 * </p>
 * <p>
 * Subscription form -&gt; Submit subscription form -&gt; Ask user to confirm -&gt; Confirm subscription
 * </p>
 * 
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Controller
@RequestMapping("/mailinglist")
public class MailingListController {
	private static final String SUBSCRIBER = "subscriber";
	private static final String UNSUBSCRIBER = "unsubscriber";
	
	@Inject private MailingListService mailingListService;

	@InitBinder(SUBSCRIBER)
	public void initSubscriberBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.setAllowedFields(new String[] { "firstName", "lastName", "email" });
	}
	
	@InitBinder(UNSUBSCRIBER)
	public void initUnsubscriberBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.setAllowedFields(new String[] { "email" });
	}
	
	
	// =================================================================================================================
	// Subscription service methods
	// =================================================================================================================
	
	/**
	 * Delivers an empty mailing list subscription form.
	 * 
	 * @param model model
	 * @return logical view name
	 */
	@RequestMapping(value = "/subscribe", method = RequestMethod.GET)
	public String getSubscribeForm(Model model) {
		model.addAttribute(new Subscriber());
		return getFullViewName("subscribeForm");
	}
	
	/**
	 * Processes subscription requests.
	 * 
	 * @param request request
	 * @param subscriber subscriber
	 * @param result binding result
	 * @return logical view name
	 */
	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public String postSubscribeForm(
			HttpServletRequest request,
			@ModelAttribute(SUBSCRIBER) @Valid Subscriber subscriber,
			BindingResult result) {
		
		if (result.hasErrors()) {
			result.reject("error.global");
			return getFullViewName("subscribeForm");
		}
		
		// Create the unconfirmed subscriber. Confirmation is a separate step. This allows us to include a subscriber ID
		// in the generated e-mail instead of having include the first name, last name and e-mail in the link we put in
		// the e-mail.
		subscriber.setIpAddress(request.getRemoteAddr());
		subscriber.setDateCreated(new Date());
		mailingListService.addSubscriber(subscriber);
		return "redirect:/mailinglist/subscribe-preconfirm.html";
	}
	
	/**
	 * Delivers a page asking the user to go to their inbox and confirm their subscription.
	 */
	@RequestMapping(value = "/subscribe-preconfirm", method = RequestMethod.GET)
	public String getConfirmSubscriptionPage() {
		return getFullViewName("subscribePreconfirm");
	}
	
	/**
	 * @param subscriberId subscriber ID
	 * @param digest digest
	 * @param model model
	 * @return logical view name
	 */
	@RequestMapping(value = "/subscribe-confirm", method = RequestMethod.GET)
	public String confirmSubscription(
			@RequestParam("s") Long subscriberId,
			@RequestParam("d") String digest,
			Model model) {
		
		try {
			mailingListService.confirmSubscriber(subscriberId, digest);
			return getFullViewName("subscribeSuccess");
		} catch (ConfirmationExpiredException e) {
			model.addAttribute("expired", true);
		} catch (ConfirmationFailedException e) {
			model.addAttribute("failed", true);
		}
		
		// Subscription failed
		model.addAttribute(new Subscriber());
		return getFullViewName("subscribeForm");
	}
	
	
	// =================================================================================================================
	// Unsubscription service methods
	// =================================================================================================================
	
	/**
	 * @param model model
	 * @return logical view name
	 */
	@RequestMapping(value = "/unsubscribe", method = RequestMethod.GET)
	public String getUnsubscribeForm(Model model) {
		model.addAttribute(new Unsubscriber());
		return getFullViewName("unsubscribeForm");
	}
	
	/**
	 * @param unsubscriber unsubscriber
	 * @param result binding result
	 * @return logical view name
	 */
	@RequestMapping(value = "/unsubscribe", method = RequestMethod.POST)
	public String postUnsubscribeForm(
			@ModelAttribute(UNSUBSCRIBER) @Valid Unsubscriber unsubscriber,
			BindingResult result) {
		
		if (result.hasErrors()) {
			result.reject("error.global");
			return getFullViewName("unsubscribeForm");
		}
		
		mailingListService.requestUnsubscription(unsubscriber.getEmail());
		return "redirect:/mailinglist/unsubscribe-preconfirm.html";
	}
	
	/**
	 * @return logical view name
	 */
	@RequestMapping(value = "/unsubscribe-preconfirm", method = RequestMethod.GET)
	public String getConfirmUnsubscriptionPage() {
		return getFullViewName("unsubscribePreconfirm");
	}
	
	/**
	 * @param email e-mail
	 * @param timestamp timestamp
	 * @param digest digest
	 * @param model model
	 * @return logical view name
	 */
	@RequestMapping(value = "/unsubscribe-confirm", method = RequestMethod.GET)
	public String confirmUnsubscription(
			@RequestParam("e") String email,
			@RequestParam("t") Long timestamp,
			@RequestParam("d") String digest,
			Model model) {
		
		try {
			mailingListService.confirmUnsubscription(email, timestamp, digest);
			return getFullViewName("unsubscribeSuccess");
		} catch (ConfirmationExpiredException e) {
			model.addAttribute("expired", true);
		} catch (ConfirmationFailedException e) {
			model.addAttribute("failed", true);
		}
		
		// Unsubscription failed
		Unsubscriber unsubscriber = new Unsubscriber();
		unsubscriber.setEmail(email);
		model.addAttribute(unsubscriber);
		return getFullViewName("unsubscribeForm");
	}
	
	
	// =================================================================================================================
	// Other
	// =================================================================================================================
	
	private String getFullViewName(String viewName) {
		return "mailingList/" + viewName;
	}
}
