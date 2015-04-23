package gate;


import gate.gui.MainFrame;
import gate.util.GateException;
public class testGate 
{
	public static void main(String[] args) throws GateException
	{
		gate.Gate.init();
		MainFrame.getInstance().setVisible(true);
	}

}
