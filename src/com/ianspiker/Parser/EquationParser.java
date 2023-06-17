package com.ianspiker.Parser;

import com.ianspiker.DataTypes.Node;

public class EquationParser {
    
    public static double parse(String equation) {

        String[] splitEq = equation.split(" ");
        
        Node head = new Node(splitEq[0], splitEq[2], splitEq[1]);
        
        int pos = 3;
        int contextNestLevel = 0;
        while (true) {

            if (contextNestLevel == 0) {

                // Moving into parenthetical context
                if (splitEq[pos + 1].equals("(")) {
                    if (pos + 4 >= splitEq.length) {
                        break;
                    }

                    head.addParentheticalContext(splitEq[pos], 
                        new Node(splitEq[pos + 2], 
                        splitEq[pos + 4], splitEq[pos + 3]));
                        
                    contextNestLevel += 1;
                    pos += 5;
                } else { // Adding to base tree
                    if (pos + 1 >= splitEq.length) {
                        break;
                    }
                    
                    head = head.addBranch(splitEq[pos], splitEq[pos + 1]);
                    pos += 2;
                }
            } else {

                // Exiting parenthetical context
                if (splitEq[pos].equals(")")) {
                    head.exitCurrentParentheticalContext();

                    contextNestLevel -= 1;
                    pos += 1;
                } else { // Adding branch in context
                    if (pos + 1 >= splitEq.length) {
                        break;
                    }

                    head.addParentheticalBranch(splitEq[pos], splitEq[pos + 1]);

                    pos += 2;
                }
            }

            if (pos >= splitEq.length) {
                break;
            }
        }

        return head.calculate();
    }
}
