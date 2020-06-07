package sbnz.rulebased.pokerbot.exceptions;

public class WrongRuleNameException extends Exception {
    public WrongRuleNameException(String errorMessage) {
        super(errorMessage);
    }
}
