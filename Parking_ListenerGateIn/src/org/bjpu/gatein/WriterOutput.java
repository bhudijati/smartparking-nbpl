/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bjpu.gatein;

/**
 *
 * @author BJPU
 */
public class WriterOutput {

    public WriterOutput() {
    }

    public void setOutputMonitoring(String AppendText) {
        outputGateInTopComponent.txtOutputListener.append(AppendText+"\n");
    }
}
