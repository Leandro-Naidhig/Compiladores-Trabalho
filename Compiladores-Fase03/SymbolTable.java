/*
    Bacharelado em Ciência da Computação

    Universidade Federal de São Carlos

    Campus Sorocaba

    Projeto Prático Fase 1 - Compiladores

    Integrantes:

    Nome: [Chady Chaito] (https://github.com/chadychaito) RA: 613697
    Nome: [Gustavo Buoro Branco de Souza] (https://github.com/Gustavobbs/) RA: 726533
    Nome: [José Gabriel Oliveira Santana] (https://github.com/Eetrexx/) RA: 620459
    Nome: [Leandro Naidhig] (https://github.com/Leandro-Naidhig/) RA: 726555

*/
import java.util.*;

public class SymbolTable {

	public SymbolTable() {
		globalTable = new Hashtable();
		localTable = new Hashtable();
	}

	public Object putInGlobal( String key, Object value ) {
		return globalTable.put(key, value);
	}

	public Object putInLocal( String key, Object value ) {
		return localTable.put(key, value);
	}

	public Object getInLocal( Object key ) {
		return localTable.get(key);
	}

	public Object getInGlobal( Object key ) {
		return globalTable.get(key);
	}

	public Object get( String key ) {
		// returns the object corresponding to the key.
		Object result;
		if ( (result = localTable.get(key)) != null ) {
			// found local identifier
			return result;
		}
		else {
			// global identifier, if it is in globalTable
			return globalTable.get(key);
		}
	}

	public void removeLocalIdent() {
		// remove all local identifiers from the table
		localTable.clear();
	}

	private Hashtable globalTable, localTable;
}
