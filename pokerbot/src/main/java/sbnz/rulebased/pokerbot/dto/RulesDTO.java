package sbnz.rulebased.pokerbot.dto;

public class RulesDTO {

    private String alias;
    private String preFlopRules;
    private String postFlopRules;

    public RulesDTO() {}

    public RulesDTO(String alias, String preFlopRules, String postFlopRules) {
        this.alias = alias;
        this.preFlopRules = preFlopRules;
        this.postFlopRules = postFlopRules;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPreFlopRules() {
        return preFlopRules;
    }

    public void setPreFlopRules(String preFlopRules) {
        this.preFlopRules = preFlopRules;
    }

    public String getPostFlopRules() {
        return postFlopRules;
    }

    public void setPostFlopRules(String postFlopRules) {
        this.postFlopRules = postFlopRules;
    }
}
