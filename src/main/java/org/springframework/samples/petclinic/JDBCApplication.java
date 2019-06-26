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
			
            System.out.println("==============");
            System.out.println("INSERTAR OWNER");
            System.out.println("==============");

			/*
			 * String sql1 =
			 * "INSERT INTO owners (first_name, last_name, address, city, telephone) VALUES ('Bartolome', 'MD', 'Calle X', 'Sevilla', '60000000')"
			 * ; ResultSet rs1 = statement.executeQuery(sql1); statement.execute(sql1);
			 */
			System.out.println("==============");
            System.out.println("INSERTAR PET");
            System.out.println("==============");
			/*
			 * String sql2 =
			 * "INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES ('Rocky', '2010-01-02', '1', '11')"
			 * ; ResultSet rs2 = statement.executeQuery(sql2); statement.execute(sql2);
			 */

			System.out.println("==============");
            System.out.println("CAMBIANDO CIUDAD");
            System.out.println("==============");
            
			String sql3 = "UPDATE owners SET city=? WHERE id=?;";
			PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
			preparedStatement3.setString(1, "Barcelona");
            preparedStatement3.setInt(2, 11);
            
			preparedStatement3.executeUpdate();
			preparedStatement3.close();

	        System.out.println("==============");
            System.out.println("CONSULTANDO OWNERS");
            System.out.println("==============");
            
            String sql = "SELECT * FROM owners";
			statement = connection.createStatement();
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
			
			System.out.println("==============");
            System.out.println("-CONSULTANDO NOMBRES-");
            System.out.println("==============");			
            
            //Statement statementConsulta = connection.createStatement(); //FORMA2
            String sql4 = "SELECT * FROM owners WHERE first_name=?"

			PreparedStatement preparedStatementConsulta = connection.prepareStatement(sql4);
            preparedStatementConsulta.setString(1, "Bartolome");
			//ResultSet rs14 = statementConsulta.executeQuery(sql4); //FORMA2

			ResultSet rs14 = preparedStatementConsulta.executeQuery();
            
			while (rs14.next()) {
				// Obtener Campos
				int id = rs14.getInt("id");
				String name = rs14.getString("first_name");
				String city = rs14.getString("city");
				// Trato resultado
				System.out.println("PERSONAS CON MISMO NOMBRE; ID: " + id + ", " + "Nombre: " + name);

			}
			rs14.close();
         
			System.out.println("==============");
            System.out.println("-RETO 5 - CREAR OWNER EN JAVA, SETEAR MASCOTA E INSERTAR EN BBDD");
            System.out.println("==============");
			
            //CREANDO OWNER
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
			
            //CREANDO TIPO PET
			PetType type = new PetType();
			type.setName("Yorsike");
			
            //CREANDO PET
			Pet pet = new Pet();
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ii:ss");
			//String stringFechaConHora = "2014-09-15 15:03:23";
            //Date fecha = sdf.parse(stringFechaConHora);

            Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
					
			pet.setBirthDate(date);
			pet.setName("Rocky");
			pet.setType(type);
            pet.setOwner(owner);
            
            String sql6 = "INSERT INTO pets (name, birth_date, type_id, owner_id) VALUES (?, ?, ?, ?)"
			PreparedStatement preparedStatement6 = connection.prepareStatement(sql6);
			preparedStatement6.setString(1, pet.getName());
			preparedStatement6.setString(2, pet.getBirthDate());
			preparedStatement6.setString(3, pet.getType());
			preparedStatement6.setString(4, pet.getOwner());

			preparedStatement6.executeUpdate();
			preparedStatement6.close();
          
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
