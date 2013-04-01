package edu.uw.netlab;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.type.Type;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class LookUpTable {
	
	class ExtendFieldDeclaration {
		FieldDeclaration field;
		String scope;
		
		public ExtendFieldDeclaration(FieldDeclaration field, String scope) {
			this.field = field;
			this.scope = scope;
		}
	}
	
	class ExtendMethodDeclaration {
		MethodDeclaration method;
		String scope;
		
		public ExtendMethodDeclaration(MethodDeclaration method, String scope) {
			this.method = method;
			this.scope = scope;
		}
	}
	
	private Hashtable<String, ClassOrInterfaceDeclaration> classLut;
	private Hashtable<String, List<ExtendFieldDeclaration>> fieldLut;
	private Hashtable<String, List<ExtendMethodDeclaration>> methodLut;
	
	public LookUpTable() {
		classLut = new Hashtable<String, ClassOrInterfaceDeclaration>();
		fieldLut = new Hashtable<String, List<ExtendFieldDeclaration>>();
		methodLut = new Hashtable<String, List<ExtendMethodDeclaration>>();
	}
	
	private void addField(FieldDeclaration field, String scope) {
		
		String type = field.getType().toString();
		
		for (VariableDeclarator var: field.getVariables()) {
			String key = var.getId().getName() + "<" + type + ">";
			if (fieldLut.get(key) == null) {
				List<ExtendFieldDeclaration> list = new LinkedList<ExtendFieldDeclaration>();
				list.add(new ExtendFieldDeclaration(field, scope));
				fieldLut.put(key, list);
			} else {
				fieldLut.get(key).add(new ExtendFieldDeclaration(field, scope));
			}
		}
	}
	
	private void addMethod(MethodDeclaration method, String scope) {
		
		String m = method.getMethodType();
		if (methodLut.get(m) == null) {
			List<ExtendMethodDeclaration> list = new LinkedList<ExtendMethodDeclaration>();
			list.add(new ExtendMethodDeclaration(method, scope));
			methodLut.put(m, list);
		} else {
			methodLut.get(m).add(new ExtendMethodDeclaration(method, scope));
		}
	}
	
	private void addTypeDeclaration(ClassOrInterfaceDeclaration typeDec, String scope) {
		
		String className = (scope == "") ? typeDec.getName() : scope + "." + typeDec.getName();
		
		if (classLut.get(className) == null)
			classLut.put(className, typeDec);
		else
			System.out.println("Class " + className + " already exists");
		
		List<BodyDeclaration> members = typeDec.getMembers();
		for (BodyDeclaration member : members) {
			if (member instanceof ClassOrInterfaceDeclaration)
				addTypeDeclaration((ClassOrInterfaceDeclaration) member, className);
			else if (member instanceof FieldDeclaration) {
				addField((FieldDeclaration) member, className);
			} else if (member instanceof MethodDeclaration) {
				addMethod((MethodDeclaration) member, className);
			}
		}
	}
	
	public void setLookUpTable(List<TypeDeclaration> typeDecList) {
		for (TypeDeclaration typeDec : typeDecList) {
			if (typeDec instanceof ClassOrInterfaceDeclaration)
				addTypeDeclaration((ClassOrInterfaceDeclaration) typeDec, "");
			else
				System.out.println("error:" + typeDec.getClass().getName());
		}
	}
}
