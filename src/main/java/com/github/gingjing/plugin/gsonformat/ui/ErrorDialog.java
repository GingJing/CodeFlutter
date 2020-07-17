package com.github.gingjing.plugin.gsonformat.ui;

import javax.swing.*;

public class ErrorDialog extends JFrame {

    private JPanel contentPane;
    private JTextPane editTP;
    private JButton okButton;
    private JScrollPane scrollPane;

    public ErrorDialog(String errorInfo) {
        setContentPane(contentPane);
        setTitle("Error Info");
        getRootPane().setDefaultButton(okButton);
        this.setAlwaysOnTop(true);
        editTP.setText(errorInfo);
        okButton.addActionListener((e) -> dispose());
        editTP.setCaretPosition(0);
    }
}
