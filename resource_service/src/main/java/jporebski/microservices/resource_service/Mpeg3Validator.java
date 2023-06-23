package jporebski.microservices.resource_service;

import org.springframework.stereotype.Component;

@Component
public class Mpeg3Validator {

    public boolean quickValidate(byte[] inputFileContents) {
        if (inputFileContents == null || inputFileContents.length < 3)
            return false;

        if (inputFileContents[0] != 'I' && inputFileContents[1] != 'D' && inputFileContents[2] != '3')
            return false;

        return true;
    }

}
