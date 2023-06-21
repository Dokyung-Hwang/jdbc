package com.example.jdbc;


import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class JdbcApplication {

	public static void main(String[] args) {
		selectAll();
	}

	public static void selectAll() {
		// Connection 객체를 자동완성으로 import할 때는 com.mysql.connection이 아닌
		// java 표준인 java.sql.Connection 클래스를 import해야 한다.
		Connection connection = null;		// 커넥션
		Statement statement = null;			// 쿼리수행
		ResultSet resultSet = null;			// 요청 응답

		try{
			// 1. 드라이버 로딩
			// 드라이버 인터페이스를 구현한 클래스를 로딩
			// mysql, oracle 등 각 벤더사 마다 클래스 이름이 다르다.
			// mysql은 "com.mysql.jdbc.Driver"이며, 이는 외우는 것이 아니라 구글링하면 된다.
			Class.forName("com.mysql.cj.jdbc.Driver");

			// 2. 연결하기
			// 드라이버 매니저에게 Connection 객체를 달라고 요청한다.
			// Connection을 얻기 위해 필요한 url 역시, 벤더사마다 다르다.
			// mysql은 "jdbc:mysql://localhost/사용할db이름"
			// DB ID/PW
			String url = "jdbc:mysql://127.0.0.1:3306/shop";
			String mysqlId = "smiledk";
			String mysqlPassword = "1112";

			// @param  getConnection(url, userName, password);
			// @return Connection
			connection = DriverManager.getConnection(url, mysqlId, mysqlPassword);
			System.out.println("연결 성공");

			// 쿼리 수행을 위한 Statement 객체 생성
			statement = connection.createStatement();

			// 4. SQL 쿼리 작성
			// 주의사항
			// 1) JDBC에서 쿼리를 작성할 때는 세미콜론(;)을 빼고 작성한다.
			// 2) SELECT 할 때 * 으로 모든 칼럼을 가져오는 것보다 가져와야 할 칼럼을 직접 명시해주는 것이 좋다.
			// 3) 원하는 결과는 쿼리로써 마무리 짓고, java 코드로 후 작업 하는 것은 권하지 않음
			// 4) 쿼리를 한 줄로 쓰기 어려운 경우 들여쓰기를 사용해도 되지만 띄어쓰기에 유의!
			String sql = "SELECT address, email, name, role FROM member";


			// 5. 쿼리 수행
			// 레코드들은 ResultSet 객체에 추가된다.
			resultSet = statement.executeQuery(sql);

			// 6. 실행결과 출력하기
			while(resultSet.next()){
				// 레코드의 칼럼은 배열과 달리 0부터 시작하지 않고 1부터 시작한다.
				// 데이터베이스에서 가져오는 데이터의 타입에 맞게 getString 또는 getInt 등을 호출한다.
				String address = resultSet.getString(1);
				String email = resultSet.getString(2);
				String name = resultSet.getString(3);
				String role = resultSet.getString(4);

				System.out.println("address: " + address + ", email: " + email + ", name: " + name + ", role: " + role);
			}

		}
		catch(ClassNotFoundException e){
			System.out.println("드라이버 로딩 실패");
		}
		catch(SQLException e){
			System.out.println("에러: " + e);
		}
		finally{
			try{
				if(connection != null && !connection.isClosed()){
					connection.close();
				}
				if(statement != null && !statement.isClosed()){
					statement.close();
				}
				if(resultSet != null && !resultSet.isClosed()){
					resultSet.close();
				}
			}
			catch( SQLException e){
				e.printStackTrace();
			}
		}
	}

}
