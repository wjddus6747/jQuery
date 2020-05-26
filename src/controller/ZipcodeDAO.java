package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ZipcodeDAO
{
	Connection con;
	PreparedStatement psmt;
	ResultSet rs;

	
	public ZipcodeDAO() {
		try {
			Context initCtx = new InitialContext();
			/*
			 * Context ctx = (Context) initCtx.lookup("java:comp/env");
			 * DataSource source = (DataSource) ctx.lookup("jdbc/myoracle");
			 */
			// 위 2번의 lookup을 아래 1번으로 병합할 수 있다.
			DataSource source = (DataSource) initCtx.lookup("java:comp/env/jdbc/myoracle");
			con = source.getConnection();
			System.out.println("DBCP연결성공");

		} catch (Exception e) {
			System.out.println("DBCP연결실패");
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			if (con != null) {
				con.close();
			}
			System.out.println("자원반납시 성공");
		} catch (Exception e) {
			System.out.println("자원반납시 예외발생");
		}
	}
	
	//우편번호를 테이블에서 시/도 가져오기
	public ArrayList<String> getSido(){
		ArrayList<String> sidoAddr = new ArrayList<String>();
		
		String sql = "select sido from zipcode "
				+ " where 1=1 "
				+ " group by sido "
				+ " order by sido asc ";
		
		try {
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery();
			while (rs.next())
			{
				sidoAddr.add(rs.getString(1));
			}
		}
		catch (Exception e) {
		}
		return sidoAddr;
	}
	
	//우편번호테이블에서 각 시/도에 해당하는 구/군 가져오기
	public ArrayList<String> getGugun(String sido)
	{
		ArrayList<String> guguAddr = new ArrayList<String>();
		
		String sql = "select distinct gugun from zipcode "
				+ " where sido = ? "
				+ " order by gugun desc";
		try
		{
			psmt = con.prepareStatement(sql);
			psmt.setString(1, sido);
			rs = psmt.executeQuery();
			while (rs.next())
			{
				guguAddr.add(rs.getString(1));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return guguAddr;
	}
}
