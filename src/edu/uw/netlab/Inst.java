package edu.uw.netlab;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.visitor.VoidVisitorAdapter;


public class Inst {
	
	private boolean compareMethod(MethodDeclaration m1, MethodDeclaration m2) {
		if (!m1.getName().equals(m2.getName())) {
			System.out.println("m1 name " + m1.getName());
			System.out.println("m2 name " + m2.getName());
			return false;
		}
			
		if (!m1.getType().toString().equals(m2.getType().toString()))
			return false;
		
		List<Parameter> paras1 = m1.getParameters();
		List<Parameter> paras2 = m2.getParameters();
		if (paras1.size() != paras2.size()) {
			System.out.println("m1 para size " + paras1.size() + "\nm2 para size " + paras2.size());
			return false;
		}
			
		for (int i = 0; i < paras1.size(); i++) {
			if (!paras1.get(i).getType().toString().equals(paras2.get(i).getType().toString())) {
				System.out.println(i + ": para type " + paras1.get(i).getType());
				System.out.println(i + ": para type " + paras2.get(i).getType());
				return false;
			}
		}
		
		return true;
	}
	
	private void SearchMethod(TypeDeclaration astTree, MethodDeclaration method) {
		List<BodyDeclaration> members = astTree.getMembers();
		for (BodyDeclaration member : members) {
			if (member instanceof MethodDeclaration) {
				if (compareMethod((MethodDeclaration) member, method))
					System.out.println("Method: " + ((MethodDeclaration) member).getName()
						+ "\npara: " + ((MethodDeclaration) member).getParameters()
						+ "\nret: " + ((MethodDeclaration) member).getType() 
						+ "\ntype para: " + ((MethodDeclaration) member).getTypeParameters()
						+ "\nthrows: " + ((MethodDeclaration) member).getThrows()
						+ "\nmodifier: " + ((MethodDeclaration) member).getModifiers());
				//System.out.println(compareMethod((MethodDeclaration) member, method));
			} else if (member instanceof TypeDeclaration) {
				SearchMethod((TypeDeclaration) member, method);
			}
		}
	}
	
	private static class MethodVisitor extends VoidVisitorAdapter {
		@Override
		public void visit(MethodDeclaration n, Object arg) {
			System.out.println(n.getName() + " " + n.getParameters());
		}
	}
	
	static public Type generateType(String typeString) {
		Type type = null;
		if (typeString == "boolean")
			type = new PrimitiveType(PrimitiveType.Primitive.Boolean);
		else if (typeString == "char")
			type = new PrimitiveType(PrimitiveType.Primitive.Char);
		else if (typeString == "byte")
			type = new PrimitiveType(PrimitiveType.Primitive.Byte);
		else if (typeString == "short")
			type = new PrimitiveType(PrimitiveType.Primitive.Short);
		else if (typeString == "int")
			type = new PrimitiveType(PrimitiveType.Primitive.Int);
		else if (typeString == "long")
			type = new PrimitiveType(PrimitiveType.Primitive.Long);
		else if (typeString == "float")
			type = new PrimitiveType(PrimitiveType.Primitive.Float);
		else if (typeString == "double")
			type = new PrimitiveType(PrimitiveType.Primitive.Double);
		else if (typeString == "void")
			type = new VoidType();
		else {
			ClassOrInterfaceType classType = new ClassOrInterfaceType(typeString);
			//type = new 
			//type = new 
			System.err.println("Can't recognize type " + typeString);
		}
		return type;
	}
	
	static public MethodDeclaration generateMethod(String methodString) {
		
		methodString = methodString.trim();
		String[] tokens = methodString.split(" |,|\\(|\\)");
		List<Parameter> pList = new LinkedList<Parameter>();
		Type returnType;
		
		for (String token : tokens)
			System.out.println(token);
		
		
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		
		FileInputStream in = new FileInputStream("input/Test.java");
		CompilationUnit cu;
		
		try { 
			cu = JavaParser.parse(in);
		} finally {
			in.close();
		}
		
		//Inst.generateMethod("void foo(int x, int y)");
		
		/*List<Parameter> pList = new LinkedList<Parameter>();
		pList.add(new Parameter(new PrimitiveType(PrimitiveType.Primitive.Int), new VariableDeclaratorId("x")));
		pList.add(new Parameter(new PrimitiveType(PrimitiveType.Primitive.Int), new VariableDeclaratorId("y")));
		
		MethodDeclaration method = new MethodDeclaration(0, 
					new PrimitiveType(PrimitiveType.Primitive.Int),
					"aaa",
					pList);
		System.out.println(method.toString());*/
	
		List<TypeDeclaration> types;
		
		/*types = cu.getTypes();
		for (TypeDeclaration type : types)
			new Inst().SearchMethod(type, method);*/
		
		
		//System.out.println(cu.getTypes());
		//List<TypeDeclaration> types = cu.getTypes();
		types = cu.getTypes();
		LookUpTable lut = new LookUpTable();
		lut.setLookUpTable(types);
		lut.dumpClassLut();
		lut.dumpFieldLut();
		lut.dumpMethodLut();
		/*for (TypeDeclaration type: types) {
			type.getClass().getName();
			System.out.println(type.getClass().getName());
			List<BodyDeclaration> members = type.getMembers();
		}
		for (TypeDeclaration type: types) {
			System.out.println(type.getClass().getName());
			List<BodyDeclaration> members = type.getMembers();
			//System.out.println(members);
			for (BodyDeclaration member : members) {
				System.out.println(member.getClass().getName());
				//System.out.println(member.toString());
				
				if (member instanceof MethodDeclaration) {
					System.out.println("Method: " + ((MethodDeclaration)member).getMethodType());
					System.out.println(((MethodDeclaration)member).toString());
					
					//Type ret = ((MethodDeclaration) member).getType();
					//System.out.println(ret.getClass().getName());
					
					//if (ret instanceof ReferenceType) {
					//	System.out.println(((ReferenceType) ret).getType().getClass().getName());
					//	System.out.println(((ReferenceType) ret).getArrayCount());
					//	ClassOrInterfaceType classType = (ClassOrInterfaceType) ((ReferenceType) ret).getType();
					//	System.out.println(classType.getName());
					//	System.out.println(classType.getScope());
					//	System.out.println(classType.getTypeArgs());
					//}
					//for (Parameter p: ((MethodDeclaration) member).getParameters()) {
					//	System.out.println(p.get)
				}
				if (member instanceof FieldDeclaration) {
					System.out.println("Field: " + ((FieldDeclaration) member));
					for (VariableDeclarator var: ((FieldDeclaration) member).getVariables()) {
						System.out.println("Var: " + var.getId().getName() + " " + 
								((FieldDeclaration) member).getType().toString());
					}
					//System.out.println(((TypeDeclaration)member).getMembers());
				}
				if (member instanceof ClassOrInterfaceDeclaration) {
					System.out.println("Class: " + ((ClassOrInterfaceDeclaration) member).getName());
					System.out.println(((ClassOrInterfaceDeclaration) member).getTypeParameters());
					System.out.println(((ClassOrInterfaceDeclaration) member).getExtends());
					System.out.println(((ClassOrInterfaceDeclaration) member).getImplements());
				}
			}
		}*/
	}
}
