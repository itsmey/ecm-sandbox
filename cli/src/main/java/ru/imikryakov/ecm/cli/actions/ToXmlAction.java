package ru.imikryakov.ecm.cli.actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ToXmlAction extends Action {
    private static Logger logger = LogManager.getLogger();

    ToXmlAction() {
        super("toXml", "export current state to XML file");
    }

    @Override
    public void perform() {
        print("Enter name of the XML file: ");
        getHierarchy().export(next(), logger);
    }
}
