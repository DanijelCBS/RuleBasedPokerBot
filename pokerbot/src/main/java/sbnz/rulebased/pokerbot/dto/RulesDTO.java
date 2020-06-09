package sbnz.rulebased.pokerbot.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class RulesDTO {

    @Pattern(regexp = "^[A-Z].*$", message = "Rules alias must start with capital letter")
    private String alias;

    @NotBlank
    private String preFlopRules;

    @NotBlank
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
