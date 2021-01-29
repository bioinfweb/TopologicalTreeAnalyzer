/*
 * Topological Tree Analyzer - User defined topological analyses of large sets of phylogenetic trees. 
 * Copyright (C) 2020-2021  Ben C. Stöver
 * <http://bioinfweb.info/TTA>
 * 
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.tta.exception;


import java.sql.SQLException;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.Token;
import org.nfunk.jep.function.PostfixMathCommand;



/**
 * This class is used to wrap {@link SQLException}s that occur during the execution of user expression functions. {@link SQLException}s can't be thrown
 * directly since {@link PostfixMathCommand#run(java.util.Stack)} only allows {@link ParseException}s.
 * 
 * @author Ben St&ouml;ver
 */
public class SQLParseException extends ParseException {
	private static final long serialVersionUID = 1L;

	
	private SQLException cause;
	
	
	public SQLParseException(SQLException cause) {
		super();
		this.cause = cause;
	}

	
	public SQLParseException(String message, SQLException cause) {
		super(message);
		this.cause = cause;
	}

	
	public SQLParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal, SQLException cause) {
		super(currentTokenVal, expectedTokenSequencesVal, tokenImageVal);
		this.cause = cause;
	}


	public SQLException getCause() {
		return cause;
	}
}
