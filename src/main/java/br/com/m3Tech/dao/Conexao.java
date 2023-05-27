//package br.com.m3Tech.dao;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import br.com.m3Tech.model.Base;
//
//
//public class Conexao {
//	
//	private static Connection con;
//	
//	private Conexao() {
//	}
//	
//	public static Connection getConnection(Base base) throws SQLException {
//		
//		try {
//			
//			if(con != null) {
//				con.close();
//			}
//			
//			con = DriverManager.getConnection("jdbc:jtds:sqlserver://"+base.getUrl(), base.getUsuario(), base.getSenha());
//		} catch (SQLException e) {
//			throw e;
//		}
//		
//		return con;
//		
//	}
//
//}
