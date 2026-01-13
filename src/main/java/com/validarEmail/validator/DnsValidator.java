package com.validarEmail.validator;

import org.springframework.stereotype.Component;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

@Component
public class DnsValidator {

    public boolean domainExists(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        String domain = email.substring(email.indexOf("@") + 1);
        return hasValidDomain(domain);
    }

    public boolean hasMxRecords(String email) {
        if (email == null || !email.contains("@")) {
            return false;
        }
        String domain = email.substring(email.indexOf("@") + 1);
        return checkMxRecords(domain);
    }

    private boolean hasValidDomain(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.A);
            lookup.run();
            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                return true;
            }
            return checkMxRecords(domain);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkMxRecords(String domain) {
        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            lookup.run();
            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                Record[] records = lookup.getAnswers();
                return records != null && records.length > 0;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
