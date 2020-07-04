package sbnz.rulebased.pokerbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import sbnz.rulebased.pokerbot.dto.RulesDTO;
import sbnz.rulebased.pokerbot.exceptions.DroolsErrorsException;
import sbnz.rulebased.pokerbot.exceptions.WrongRuleNameException;
import sbnz.rulebased.pokerbot.services.RulesService;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Component
public class Test {

    @Autowired
    RulesService rulesService;

    /*@PostConstruct
    public void test() throws IOException, WrongRuleNameException, ParserConfigurationException, TransformerException, SAXException, DroolsErrorsException {
        String preFlop = "rule \"Determine suits pre-flop\"\n" +
                "\twhen\n" +
                "        $ps: PlayerState($card1: card1, $card2: card2, $card1.getSuit() == $card2.getSuit())\n" +
                "    then\n" +
                "        System.out.println(\"Rule from proba: \" + drools.getRule().getName());\n" +
                "        modify($ps) {\n" +
                "            setSameSuit(true)\n" +
                "        }\n" +
                "end\n" +
                "\n" +
                "rule \"Always checkOrFold pre-flop\"\n" +
                "\twhen\n" +
                "        $ps: PlayerState()\n" +
                "    then\n" +
                "        System.out.println(\"Rule from proba: \" + drools.getRule().getName());\n" +
                "        modify($ps) {\n" +
                "            setActionToTake(Action.checkOrFoldAction(gameInfo))\n" +
                "        }\n" +
                "end";

        String postFlop = "rule \"Always checkOrFold post-flop\"\n" +
                "\twhen\n" +
                "        $ps: PlayerState($card1: card1, $card2: card2, !results.isCalculated())\n" +
                "    then\n" +
                "        System.out.println(\"Rule from proba: \" + drools.getRule().getName());\n" +
                "        modify($ps) {\n" +
                "            setActionToTake(Action.checkOrFoldAction(gameInfo))\n" +
                "        }\n" +
                "end";

        RulesDTO rulesDTO = new RulesDTO("test", preFlop, postFlop);
        System.out.println(rulesService.createRules(rulesDTO));
    }*/
}
