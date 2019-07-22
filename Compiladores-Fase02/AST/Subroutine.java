package AST;
import java.io.*;

abstract public class Subroutine {
	abstract public void genC(PW pw);

	public String getName() {
		return id.getName();
	}

	public void setParamList(ParamList parametros) {
		this.parametros = parametros;
	}

	public ParamList getParamList() {
		return parametros;
	}

	public int getNumParam() {
    	return this.parametros.getSize();
  	}

	public void setStatList(StatList statlist) {
		this.statlist = statlist;
	}

	public StatList getStatList() {
		return statlist;
	}

	protected Variable id;
	private ParamList parametros;
	private StatList statlist;
}
