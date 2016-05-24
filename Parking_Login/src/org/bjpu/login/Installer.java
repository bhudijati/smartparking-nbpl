/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjpu.login;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import org.openide.*;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    private LoginForm login = new LoginForm();

    @Override
    public void restored() {
        createDialog();
    }

    private void createDialog() {
        JButton ok = new JButton();
        ok.setText("Login");
        JButton cancel = new JButton();
        cancel.setText("Exit");
        cancel.addActionListener((ActionEvent ae) -> {
            exit();
        });
        ok.addActionListener((ActionEvent arg0) -> {
//            authenticate();
        });
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(login, "Login Smart Parking System");
        DialogDisplayer.getDefault().notifyLater(nd);
        nd.setOptions(new Object[]{ok, cancel});
        nd.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            if (NotifyDescriptor.CLOSED_OPTION.equals(pce.getNewValue())) {
                exit();
            }
        });
    }

    private void exit() {
        LifecycleManager.getDefault().exit();
    }
}
