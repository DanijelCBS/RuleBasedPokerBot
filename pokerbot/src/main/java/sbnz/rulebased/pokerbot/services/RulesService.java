package sbnz.rulebased.pokerbot.services;

import org.drools.core.io.impl.ReaderResource;
import org.drools.verifier.Verifier;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.kie.api.io.ResourceType;
import org.omg.CORBA.WrongTransaction;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import sbnz.rulebased.pokerbot.dto.RulesDTO;
import sbnz.rulebased.pokerbot.exceptions.DroolsErrorsException;
import sbnz.rulebased.pokerbot.exceptions.WrongRuleNameException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RulesService {

    public String createRules(RulesDTO rules) throws WrongRuleNameException, IOException, ParserConfigurationException, TransformerException, SAXException, DroolsErrorsException {
        VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
        Verifier verifier = vBuilder.newVerifier();
        StringBuilder stringBuilder = new StringBuilder();

        if (verifyRules(stringBuilder, rules.getPreFlopRules(), "PRE-FLOP", verifier)
                || verifyRules(stringBuilder, rules.getPostFlopRules(), "POST-FLOP", verifier)) {
            throw new DroolsErrorsException(stringBuilder.toString());
        }

        String preFlopDRL = processPreFlopRules(rules.getAlias(), rules.getPreFlopRules());
        String postFlopDRL = processPostFlopRules(rules.getAlias(), rules.getPostFlopRules());

        String rulesFolderPath = "../opentestbed/src/main/resources/" + rules.getAlias();
        new File(rulesFolderPath).mkdirs();

        try (PrintWriter out = new PrintWriter(rulesFolderPath + "/pre-flop-rules.drl")) {
            out.println(preFlopDRL);
        }

        try (PrintWriter out = new PrintWriter(rulesFolderPath + "/post-flop-rules.drl")) {
            out.println(postFlopDRL);
        }

        updateKModuleXML(rules.getAlias());

        return "Successfully created rules";
    }

    private boolean verifyRules(StringBuilder stringBuilder, String rules, String phase, Verifier verifier) {
        verifier.addResourcesToVerify(new ReaderResource(new StringReader(rules)), ResourceType.DRL);
        stringBuilder.append(phase).append("\n");
        if (verifier.getErrors().size() != 0)
        {
            for (int i = 0; i < verifier.getErrors().size(); i++)
            {
                stringBuilder.append(verifier.getErrors().get(i).getMessage()).append("\n\n");
            }
            return true;
        }
        else {
            stringBuilder.append("No errors");
            return false;
        }
    }

    private String processPreFlopRules(String alias, String preFlopRules) throws WrongRuleNameException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(alias).append("\n").append("\n");

        stringBuilder.append(getPreFlopImportsAndGlobals());
        Pattern rulePattern = Pattern.compile("rule \"[a-zA-Z0-9 +.;:-]*\"");
        Matcher m = rulePattern.matcher(preFlopRules);
        boolean found = false;
        String rule;
        while (m.find()) {
            found = true;
            rule = m.group(0);
            if (rule.length() < 8 || !rule.contains("-"))
                throw new WrongRuleNameException("Rule name must contains 'pre-flop'");
            String[] tokens = rule.split("-");
            String pre = tokens[tokens.length - 2];
            pre = pre.substring(pre.length() - 3);
            String flop = tokens[tokens.length - 1];
            flop = flop.substring(0, flop.length() - 1);
            if (!pre.equals("pre") || !flop.equals("flop"))
                throw new WrongRuleNameException("Rule name must end with 'pre-flop'");
        }

        if (!found)
            throw new WrongRuleNameException("Unsupported characters in rule name");

        stringBuilder.append(preFlopRules);

        return stringBuilder.toString();
    }

    private String processPostFlopRules(String alias, String postFlopRules) throws WrongRuleNameException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(alias).append("\n");

        stringBuilder.append(getPostFlopImportsAndGlobals());
        Pattern rulePattern = Pattern.compile("rule \"[a-zA-Z0-9 +.;:-]*\"");
        Matcher m = rulePattern.matcher(postFlopRules);
        String rule;
        boolean found = false;
        while (m.find()) {
            found = true;
            rule = m.group(0);
            if (rule.length() < 9 || !rule.contains("-"))
                throw new WrongRuleNameException("Rule name must contains 'post-flop'");
            String[] tokens = rule.split("-");
            String post = tokens[tokens.length - 2];
            post = post.substring(post.length() - 4);
            String flop = tokens[tokens.length - 1];
            flop = flop.substring(0, flop.length() - 1);
            if (!post.equals("post") || !flop.equals("flop"))
                throw new WrongRuleNameException("Rule name must end with 'post-flop'");
        }

        if (!found)
            throw new WrongRuleNameException("Unsupported characters in rule name");

        stringBuilder.append(postFlopRules);

        return stringBuilder.toString();
    }

    private String getPreFlopImportsAndGlobals() {
        return "import com.biotools.meerkat.Card;\n" +
                "import com.biotools.meerkat.Action;\n" +
                "import com.biotools.meerkat.GameInfo;\n" +
                "import bots.rulebasedbot.Utility;\n" +
                "import bots.rulebasedbot.PlayerState;\n" +
                "import bots.rulebasedbot.PlayStyle;\n" +
                "import bots.rulebasedbot.Strategy;\n" +
                "import com.biotools.meerkat.Holdem;\n" +
                "\n" +
                "global Integer make1Threshold;\n" +
                "global Integer make2Threshold;\n" +
                "global Integer make4Threshold;\n" +
                "global Integer call1Threshold;\n" +
                "global Integer call2Threshold;\n" +
                "global Integer numOfPlayersToAct;\n" +
                "global GameInfo gameInfo;\n\n";

    }

    private String getPostFlopImportsAndGlobals() {
        return "import com.biotools.meerkat.Card;\n" +
                "import com.biotools.meerkat.Action;\n" +
                "import com.biotools.meerkat.GameInfo;\n" +
                "import bots.rulebasedbot.Utility;\n" +
                "import bots.rulebasedbot.PlayerState;\n" +
                "import bots.rulebasedbot.PlayStyle;\n" +
                "import bots.rulebasedbot.Strategy;\n" +
                "import com.biotools.meerkat.Holdem;\n" +
                "import bots.rulebasedbot.BettingEvent;\n" +
                "import bots.rulebasedbot.HandStrengthEnum;\n" +
                "\n" +
                "global Double make1PostFlopThreshold;\n" +
                "global Double make2PostFlopThreshold;\n" +
                "global Double potOdds;\n" +
                "global Double potOdds2;\n" +
                "global Boolean semiBluffingFlag;\n" +
                "global Double showdownCost;\n" +
                "global Double showdownOdds;\n" +
                "global GameInfo gameInfo;\n\n";
    }

    private void updateKModuleXML(String alias) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        String kModulePath = "../opentestbed/src/main/resources/META-INF/kmodule.xml";
        File file = new File(kModulePath);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        Element kBase = document.createElement("kbase");
        Element kSession = document.createElement("ksession");
        kSession.setAttribute("name", "ksession-" + alias);
        kBase.setAttribute("name", alias);
        kBase.setAttribute("packages", "resources." + alias);
        kBase.appendChild(kSession);

        document.getDocumentElement().appendChild(kBase);

        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        tr.transform(new DOMSource(document),
                new StreamResult(new FileOutputStream(kModulePath)));
    }
}
