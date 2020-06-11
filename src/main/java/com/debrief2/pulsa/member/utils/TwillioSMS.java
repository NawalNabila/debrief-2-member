package com.debrief2.pulsa.member.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwillioSMS {
    public void send(String myTwilioPhoneNumber, String twilioAccountSid, String twilioAuthToken, String phone, String code){
        Twilio.init(twilioAccountSid, twilioAuthToken);
        Message.creator(
                new PhoneNumber("+"+phone),
                new PhoneNumber(myTwilioPhoneNumber),
                "Kode OTP: "+code).create();
    }
}
