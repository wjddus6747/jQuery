package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class OracleDAO
{
	Connection con;
	PreparedStatement psmt;
	ResultSet rs;

	public OracleDAO()
	{
		try
		{
			Context initCtx = new InitialContext();
			/*
			 * Context ctx = (Context) initCtx.lookup("java:comp/env"); DataSource source =
			 * (DataSource) ctx.lookup("jdbc/myoracle");
			 */
			// 위 2번의 lookup을 아래 1번으로 병합할 수 있다.
			DataSource source = (DataSource) initCtx.lookup("java:comp/env/jdbc/myoracle");
			con = source.getConnection();
			System.out.println("DBCP연결성공");

		} catch (Exception e)
		{
			System.out.println("DBCP연결실패");
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
			if (psmt != null)
			{
				psmt.close();
			}
			if (con != null)
			{
				con.close();
			}
			System.out.println("자원반납시 성공");
		} catch (Exception e)
		{
			System.out.println("자원반납시 예외발생");
		}
	}

	public boolean isMember(String id, String pass)
	{
		String sql = "select count(*) from member where id=? AND pass=?";
		int isMember = 0;
		boolean isFlag = false;

		try
		{
			// prepare 객체로 쿼리문 전송
			psmt = con.prepareStatement(sql);
			// 인파라미터 설정
			psmt.setString(1, id);
			psmt.setString(2, pass);
			// 쿼리실행
			rs = psmt.executeQuery();
			// 실행결과를 가져오기 위해 next()호출
			rs.next();

			isMember = rs.getInt(1);
			System.out.println("affected:" + isMember);
			if (isMember == 0)
			{
				isFlag = false;
			} else
			{
				isFlag = true;
			}
		} catch (Exception e)
		{
			isFlag = false;
			e.printStackTrace();
		}
		return isFlag;
	}
}
