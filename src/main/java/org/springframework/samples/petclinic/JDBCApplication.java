package org.springframework.samples.petclinic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;

public class JDBCApplication {

	public static void main(String[] args) {
		System.out.println("-------- Test de conexión con MySQL ------------");
		// Comprobando driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No encuentro el driver en el Classpath");
			e.printStackTrace();
			return;
		}

		System.out.println("Driver instalado y funcionando");
		Connection connection = null;
		Statement statement = null;
		// Abriendo conexión
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petclinic", "root", "root");
			if (connection != null)
				System.out.println("Conexión establecida");

			System.out.println("-----INSERTAR OWNER------");

			/*
			 * String sql1 =
			 * "INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES ('Bartolome', 'MD', 'Calle X', 'Sevilla', '60000000')"
			 * ; ResultSet rs1 = statement.executeQuery(sql1); statement.execute(sql1);
			 */

			System.out.println("-----INSERTAR PETS------");
			/*
			 * String sql2 =
			 * "INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Rocky', '2010-01-02', '1', '11')"
			 * ; ResultSet rs2 = statement.executeQuery(sql2); statement.execute(sql2);
			 */

			System.out.println("-----CAMBIANDO CIUDAD------");
			// String sql3 = "SELECT * FROM owners WHERE id=?";
			PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE owners SET city=? WHERE id=11;");

			preparedStatement3.setString(1, "Barcelona");
			preparedStatement3.executeUpdate();
			preparedStatement3.close();

			System.out.println("-----CONSULTADO OWNERS------");

			// Consulta
			statement = connection.createStatement();
			String sql = "SELECT * FROM owners";
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				// Obtener Campos
				int id = rs.getInt("id");
				String name = rs.getString("first_name");
				String city = rs.getString("city");

				// Trato resultado
				System.out.println("PERSONA con ID: " + id + ", " + "Nombre: " + name + ", " + "City: " + city);

			}
			rs.close();
			
			
			System.out.println("-----CONSULTANDO NOMBRES------");
			
			/*String sql4 = "SELECT * FROM owners WHERE first_name=?";
			
			PreparedStatement preparedStatement4 = connection.prepareStatement(sql4);
			preparedStatement4.setString(1, "Bartolome");
			
			ResultSet rs4 = preparedStatement4.executeQuery(sql4);
			rs4.next();
			
			//System.out.println("PERSONA conincidentes: " + rs4.getNString("first_name"));
			rs4.close();*/
			
			System.out.println("-----RETO5------");
			
			Owner owner = new Owner();
			owner.setLastName("MD");
			owner.setFirstName("Pepito");
			owner.setCity("Sevilla");
			owner.setAddress("Calle X2");
			owner.setTelephone("6000000000");
			
			String sql5 = "INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES (?, ?, ?, ?, ?)";
	
			PreparedStatement preparedStatement5 = connection.prepareStatement(sql5);

			preparedStatement5.setString(1, owner.getFirstName());
			preparedStatement5.setString(2, owner.getLastName());
			preparedStatement5.setString(3, owner.getAddress());
			preparedStatement5.setString(4, owner.getCity());
			preparedStatement5.setString(5, owner.getTelephone());

			preparedStatement5.executeUpdate();
			preparedStatement5.close();
			
			PetType type = new PetType();
			type.setName("Yorsike");
			
			Pet pet = new Pet();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ii:ss");
			String stringFechaConHora = "2014-09-15 15:03:23";
			//Date fecha = sdf.parse(stringFechaConHora);
					
			//pet.setBirthDate(fecha);
			pet.setName("Rocky");
			pet.setType(type);
			
			
			
			/*
			String sql4 = "SELECT * FROM owners WHERE id=?";
			ResultSet rs4 = statement.executeQuery(sql4);
			PreparedStatement preparedStatement4 = connection.prepareStatement(sql4);

			preparedStatement4.setString(1, "Barcelona");
			preparedStatement4.executeUpdate();
			preparedStatement4.close();*/

			// Cerrando conexión
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		} finally {
			try {
				if (statement != null)
					connection.close();
			} catch (SQLException se) {

			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}
