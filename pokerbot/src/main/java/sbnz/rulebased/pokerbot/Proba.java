package sbnz.rulebased.pokerbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import sbnz.rulebased.pokerbot.dto.RulesDTO;
import sbnz.rulebased.pokerbot.exceptions.WrongRuleNameException;
import sbnz.rulebased.pokerbot.services.RulesService;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class Proba {

    @Autowired
    RulesService rulesService;

    @PostConstruct
    public void proba() throws IOException, WrongRuleNameException, ParserConfigurationException, TransformerException, SAXException {
        String preFlop = "rule \"Determine suits pre-flop\"\n" +
                "    when\n" +
                "        $ps: PlayerState($card1: card1, $card2: card2, $card1.getSuit() == $card2.getSuit())\n" +
                "    then\n" +
                "        System.out.println(\"Rule: \" + drools.getRule().getName());\n" +
                "        modify($ps) {\n" +
                "            setSameSuit(true)\n" +
                "        }\n" +
                "end\n" +
                "\n" +
                "rule \"Starting hand rank - number of players is 5+, cards have same suit pre-flop\"\n" +
                "    when\n" +
                "        $ps: PlayerState(expectedNumOfPlayers >= 5, sameSuit == true, $card1: card1, $card2: card2)\n" +
                "    then\n" +
                "        System.out.println(\"Rule: \" + drools.getRule().getName());\n" +
                "        modify($ps) {\n" +
                "            setIR($card1.getRank() > $card2.getRank() ? Utility.IR7.get($card1.getRank()).get($card2.getRank()) :\n" +
                "                            Utility.IR7.get($card2.getRank()).get($card1.getRank()))\n" +
                "        }\n" +
                "end";

        String postFlop = "rule \"Calculate potentials and hand rank post-flop\"\n" +
                "    when\n" +
                "        $ps: PlayerState($card1: card1, $card2: card2, !results.isCalculated())\n" +
                "    then\n" +
                "        System.out.println(\"Rule: \" + drools.getRule().getName());\n" +
                "        modify($ps) {\n" +
                "            setResults(Utility.enumerateHands($card1, $card2, gameInfo))\n" +
                "        }\n" +
                "end";

        // RulesDTO rulesDTO = new RulesDTO("proba", preFlop, postFlop);
        // System.out.println(rulesService.createRules(rulesDTO));
    }
}
