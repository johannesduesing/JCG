/*
 * BSD 2-Clause License:
 * Copyright (c) 2009 - 2016
 * Software Technology Group
 * Department of Computer Science
 * Technische Universität Darmstadt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package app;

import static lib.annotations.callgraph.CallGraphAlgorithm.CHA;
import static lib.annotations.documentation.CGCategory.*;

import lib.annotations.callgraph.InvokedConstructor;
import lib.annotations.properties.EntryPoint;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.awt.EventQueue;

import lib.*;

import static lib.annotations.callgraph.AnalysisMode.*;

/**
 * This class defines an application which saves an expression to a file via the
 * serialization mechanism and then loads them.
 * 
 * This application class performs serialization and deserialization of serializable
 * classes thus making the serialization-specific methods of those classes entrypoints 
 * in an application scenario.
 * <p>
 * <b>NOTE</b><br>
 * This class is not meant to be (automatically) recompiled; it just serves
 * documentation purposes.
 * <p>
 * <!--
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * INTENTIONALLY LEFT EMPTY TO MAKE SURE THAT THE SPECIFIED LINE NUMBERS ARE
 * STABLE IF THE CODE (E.G. IMPORTS) CHANGE.
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * -->
 * 
 * @author Michael Eichberg
 * @author Michael Reif
 * @author Roberts Kolosovs
 */
public class ExpressionSaver {

	@EntryPoint(value = { DESKTOP_APP, OPA, CPA })
	@InvokedConstructor(receiverType = "lib/SerializableConstant", line = 87)
	@InvokedConstructor(receiverType = "lib/ExternalizableConstant", line = 88)
	public static void main(final String[] args) {
		Constant serializableConst = new SerializableConstant(42);
		ExternalizableConstant externalizableConst = new ExternalizableConstant(42);

		try {
			serializableConst = (Constant) load("const.ser");
			externalizableConst = (ExternalizableConstant) load("extConst.ser");
			
			save(serializableConst, "const.ser");
			save(externalizableConst, "extConst.ser");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	private static void save(Object obj, String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(obj);
		out.close();
	}
	
	private static Object load(String fileName) throws IOException, ClassNotFoundException {
		Object obj = null;
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream in = new ObjectInputStream(fis);
		obj = in.readObject();
		in.close();
		return obj;
	}
}
