package gui.api;

import exceptions.OrderException;
import exceptions.WalletException;

public interface CommandLineInterface {

    public void run() throws WalletException, OrderException; //todo: [Review] public нужен? И где джава доки?
}
