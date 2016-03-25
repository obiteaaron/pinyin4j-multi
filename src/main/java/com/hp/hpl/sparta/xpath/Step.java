/* Generated by Together */

package com.hp.hpl.sparta.xpath;

import java.io.*;

/**
 * One of the steps which, separated by slashes, make up an XPath
 * expression.

 <blockquote><small> Copyright (C) 2002 Hewlett-Packard Company.
 This file is part of Sparta, an XML Parser, DOM, and XPath library.
 This library is free software; you can redistribute it and/or
 modify it under the terms of the <a href="doc-files/LGPL.txt">GNU
 Lesser General Public License</a> as published by the Free Software
 Foundation; either version 2.1 of the License, or (at your option)
 any later version.  This library is distributed in the hope that it
 will be useful, but WITHOUT ANY WARRANTY; without even the implied
 warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 PURPOSE. </small></blockquote>
 @version  $Date: 2003/07/18 00:01:42 $  $Revision: 1.3 $
 @author Eamonn O'Brien-Strain
 */
public class Step {

    public static Step DOT = new Step(ThisNodeTest.INSTANCE, TrueExpr.INSTANCE);

    Step(NodeTest nodeTest, BooleanExpr predicate) {
        nodeTest_ = nodeTest;
        predicate_ = predicate;
        multiLevel_ = false;
    }

    /**
       @precondition current token is 1st token in the node_test production.
       @postcondition current tok is tok after last tok of step production */
    Step(XPath xpath, boolean multiLevel, SimpleStreamTokenizer toks) throws XPathException,
            IOException {
        multiLevel_ = multiLevel;

        switch (toks.ttype) {
            case '.':
                if (toks.nextToken() == '.')
                    nodeTest_ = ParentNodeTest.INSTANCE;
                else {
                    toks.pushBack();
                    nodeTest_ = ThisNodeTest.INSTANCE;
                }
                break;
            case '*':
                nodeTest_ = AllElementTest.INSTANCE;
                break;
            case '@':
                if (toks.nextToken() != SimpleStreamTokenizer.TT_WORD)
                    throw new XPathException(xpath, "after @ in node test", toks, "name");
                nodeTest_ = new AttrTest(toks.sval);
                break;
            case SimpleStreamTokenizer.TT_WORD:
                if (toks.sval.equals("text")) {
                    if (toks.nextToken() != '(' || toks.nextToken() != ')')
                        throw new XPathException(xpath, "after text", toks, "()");
                    nodeTest_ = TextTest.INSTANCE;
                } else
                    nodeTest_ = new ElementTest(toks.sval);
                break;
            default:
                throw new XPathException(xpath, "at begininning of step", toks,
                        "'.' or '*' or name");
        }
        if (toks.nextToken() == '[') {
            toks.nextToken();
            //current token is first token in expr production
            predicate_ = ExprFactory.createExpr(xpath, toks);
            //current token is 1st token after expr production
            if (toks.ttype != ']')
                throw new XPathException(xpath, "after predicate expression", toks, "]");
            toks.nextToken();
        } else
            predicate_ = TrueExpr.INSTANCE;
        //current token is token after step production
    }

    public String toString() {
        return nodeTest_.toString() + predicate_.toString();
    }

    /** Is this step preceeded by a '//' ? */
    public boolean isMultiLevel() {
        return multiLevel_;
    }

    /** Does this step evaluate to a string values (attribute values
        or text() nodes)*/
    public boolean isStringValue() {
        return nodeTest_.isStringValue();
    }

    public NodeTest getNodeTest() {
        return nodeTest_;
    }

    public BooleanExpr getPredicate() {
        return predicate_;
    }

    /**
     * @link aggregationByValue
     * @directed
     */
    private final NodeTest nodeTest_;

    /**
     * @link aggregationByValue
     * @directed
     * @label predicate
     */
    private final BooleanExpr predicate_;

    private final boolean multiLevel_;
}

// $Log: Step.java,v $
// Revision 1.3  2003/07/18 00:01:42  eobrain
// Make compatiblie with J2ME.  For example do not use "new"
// java.util classes.
//
// Revision 1.2  2002/12/06 23:39:07  eobrain
// Make objects that are always the same follow the Flyweight Pattern.
//
// Revision 1.1.1.1  2002/08/19 05:04:04  eobrain
// import from HP Labs internal CVS
//
// Revision 1.6  2002/08/18 23:38:59  eob
// Add copyright and other formatting and commenting in preparation for
// release to SourceForge.
//
// Revision 1.5  2002/06/14 19:41:29  eob
// Add handling of "text()" in XPath expressions.
//
// Revision 1.4  2002/06/04 05:28:00  eob
// Simplify use of visitor pattern to make code easier to understand.
//
// Revision 1.3  2002/05/23 21:13:43  eob
// Better error reporting.
//
// Revision 1.2  2002/02/04 22:12:14  eob
// Add handling of nodetest for attribute.
//
// Revision 1.1  2002/02/01 02:04:23  eob
// initial
