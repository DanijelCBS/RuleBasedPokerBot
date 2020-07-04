package sbnz.rulebased.pokerbot.services;

import org.drools.core.io.impl.ReaderResource;
import org.drools.verifier.Verifier;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.kie.api.io.ResourceType;
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
        if (rules.getAlias().equals("rules"))
            throw new WrongRuleNameException("Rules alias can not be 'rules'");

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

    private String processPreFlopRules(String alias, String preFlopRules) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(alias).append("\n").append("\n");

        String salience = "salience 100*phase";
        stringBuilder.append(getPreFlopImportsAndGlobals());
        Pattern rulePattern = Pattern.compile("when");
        Matcher m = rulePattern.matcher(preFlopRules);
        String newPreFlopRules = m.replaceAll(salience + "\n\twhen");

        stringBuilder.append(newPreFlopRules);

        return stringBuilder.toString();
    }

    private String processPostFlopRules(String alias, String postFlopRules) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package ").append(alias).append("\n");

        String salience = "salience -100*phase";
        stringBuilder.append(getPostFlopImportsAndGlobals());
        Pattern rulePattern = Pattern.compile("when");
        Matcher m = rulePattern.matcher(postFlopRules);
        String newPostFlopRules = m.replaceAll(salience + "\n\twhen");

        stringBuilder.append(newPostFlopRules);

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
                "import bots.rulebasedbot.Thresholds;\n" +
                "\n" +
                "global Integer numOfPlayersToAct;\n" +
                "global Integer phase;\n" +
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
                "import bots.rulebasedbot.PostFlopParameters;\n" +
                "\n" +
                "global Double make1PostFlopThreshold;\n" +
                "global Double make2PostFlopThreshold;\n" +
                "global Integer phase;\n" +
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
