import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;


public class Inst {
	
	private boolean compareMethod(MethodDeclaration m1, MethodDeclaration m2) {
		if (m1.getName() != m2.getName())
			return false;
		if (m1.getType().toString() != m2.getType().toString())
				return false;
		
		List<Parameter> paras1 = m1.getParameters();
		List<Parameter> paras2 = m2.getParameters();
		if (paras1.size() != paras2.size())
			return false;
		for (int i = 0; i < paras1.size(); i++) {
			if (paras1.get(i).getType().toString() != paras2.get(i).getType().toString())
				return false;
		}
		
		return true;
	}
	
	private void DFS(TypeDeclaration type) {
		List<BodyDeclaration> members = type.getMembers();
		for (BodyDeclaration member : members) {
			if (member instanceof MethodDeclaration) {
				System.out.println("Method: " + ((MethodDeclaration) member).getName()
					+ "\npara: " + ((MethodDeclaration) member).getParameters()
					+ "\nret: " + ((MethodDeclaration) member).getType() 
					+ "\ntype para: " + ((MethodDeclaration) member).getTypeParameters()
					+ "\nthrows: " + ((MethodDeclaration) member).getThrows()
					+ "\nmodifier: " + ((MethodDeclaration) member).getModifiers());
			} else if (member instanceof TypeDeclaration) {
				DFS((TypeDeclaration) member);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		FileInputStream in = new FileInputStream("input/Test.java");
		CompilationUnit cu;
		
		try { 
			cu = JavaParser.parse(in);
		} finally {
			in.close();
		}
		
		List<Parameter> pList = new List<Parameter>();
		pList.add(new Parameter(new PrimitiveType(PrimitiveType.Primitive.Int), "x"));
		pList.add(new Parameter(new PrimitiveType(PrimitiveType.Primitive.Int), "y"));
		MethodDeclaration method = new MethodDeclaration(0, 
					new PrimitiveType(PrimitiveType.Primitive.Int),
					"aaa",
					pList);
		
		List<TypeDeclaration> types = cu.getTypes();
		for (TypeDeclaration type : types)
			new Inst().DFS(type);
		
		
		//System.out.println(cu.getTypes());
		//List<TypeDeclaration> types = cu.getTypes();
		/*for (TypeDeclaration type : types) {
			List<BodyDeclaration> members = type.getMembers();
			System.out.println(members);
			for (BodyDeclaration member : members) {
				if (member instanceof MethodDeclaration) {
					System.out.println(member.toString());
				}
				if (member instanceof TypeDeclaration) {
					System.out.println(((TypeDeclaration)member).getMembers());
				}
			}
		}*/
	}
	
	private static class MethodVisitor extends VoidVisitorAdapter {
		@Override
		public void visit(MethodDeclaration n, Object arg) {
			System.out.println(n.getName() + " " + n.getParameters());
		}
	}
}
