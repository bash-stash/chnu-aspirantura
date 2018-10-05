package com.chnu.aspirantura;

import com.chnu.aspirantura.aspirant.*;
import com.chnu.aspirantura.discipline.ObjectDiscipline;
import com.chnu.aspirantura.discipline.ObjectStudyForm;
import com.chnu.aspirantura.discipline.ObjectTypeDiscipl;
import com.chnu.aspirantura.faculty.ObjectFaculty;
import com.chnu.aspirantura.kafedra.ObjectKafedra;
import com.chnu.aspirantura.nakaz.ObjectNakaz;
import com.chnu.aspirantura.nakaz.ObjectTypeNakazi;
import com.chnu.aspirantura.speciality.ObjectSpeciality;
import com.chnu.aspirantura.vykladach.ObjectVykladach;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class SqlCommander {


    private static String url;
    private static String name;
    private static String password;

    static {

        Settings.loadDatabaseProperties();

        try {
            url = "jdbc:mysql://" + Settings.getDatabaseProperty("database.host") + ":" + Settings.getDatabaseProperty("database.port") + "/" + Settings.getDatabaseProperty("database.name") + "?characterEncoding=utf8&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            name = Settings.getDatabaseProperty("database.user");
            password = Settings.getDatabaseProperty("database.pass");
            if (password.equals("aspirantura")) password = "aspirant_chnu_27&&";
            Utils.logger.debug("Cfg is set up successfully, trying to connect..");
            DriverManager.getConnection(url, name, password);
            Utils.logger.debug("Connected successfully!");
        } catch (Exception e) {
            Utils.logger.debug("Connection failed: " + e.getMessage());
        }
    }


    public static void writeLog(String toWrite) {
        Utils.dbLogger.debug(toWrite);
    }

    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, name, password);
        } catch (Exception e) {

        }

        return connection;
    }


    /************************************************
     AUTHORIZATION BLOCK
     ************************************************/
    public static int login(String login, String passwordLocal) {
        Connection connection = getConnection();
        try {
            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT password FROM authorization WHERE login='" + login + "'");

            while (result1.next()) {
                if (result1.getString("password").equals(passwordLocal)) {
                    return 1;
                } else {
                    //password is wrong
                    return -1;
                }
            }

            if (false) throw new ConnectException();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -2;
        } catch (ConnectException exe) {
            return -2;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    /************************************************
     FACULTY BLOCK
     ************************************************/

    public static ObservableList<ObjectFaculty> getAllFaculty() {
        Connection connection = getConnection();
        ObservableList<ObjectFaculty> list = FXCollections.observableArrayList();

        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT * FROM faculty");

            while (result1.next()) {
                list.add(new ObjectFaculty(result1.getInt("id"), result1.getString("name"), result1.getString("status")));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static void addNewFaculty(String nameFaculty) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "INSERT INTO faculty(name,status) VALUES(?,'Активно')";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameFaculty);
            preparedStatement.execute();


            writeLog("INSERT INTO faculty(name,status) VALUES('" + nameFaculty + "','Активно')");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    public static void editFaculty(int id, String newName, String status) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "UPDATE faculty SET name = ?,status=? WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, status);
            preparedStatement.setInt(3, id);
            preparedStatement.execute();

            writeLog("UPDATE faculty SET name = '" + newName + "',status='" + status + "' WHERE id=" + id);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static ObjectFaculty getFaculty(int idX) {
        Connection connection = getConnection();
        ObjectFaculty s = null;
        try {

            PreparedStatement preparedStatement;
            String query = "SELECT name,id,status FROM faculty WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                s = new ObjectFaculty(result1.getInt("id"), result1.getString("name"), result1.getString("status"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return s;
    }


    /************************************************
     NAKAZ BLOCK
     ************************************************/


    public static ObservableList<ObjectNakaz> getAllNakazi() {
        Connection connection = getConnection();
        ObservableList<ObjectNakaz> list = FXCollections.observableArrayList();
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT nakaz.id, nakaz.about, nakaz.nom_nakaz, nakaz.date_nakaz, nakaz_type.value " +
                    "FROM nakaz JOIN nakaz_type ON nakaz.type = nakaz_type.type ORDER BY id ASC ");


            java.util.Date date;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");

            while (result1.next()) {
                date = originalFormat.parse(result1.getDate("date_nakaz").toString());
                date = targetFormat.parse(targetFormat.format(date));

                list.add(new ObjectNakaz(result1.getInt("id"), result1.getString("nom_nakaz"),
                        date, result1.getString("value")));

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static String getAboutNakaz(int idX) {
        Connection connection = getConnection();
        String s = "";
        try {

            PreparedStatement preparedStatement;
            String query = "SELECT about FROM nakaz WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                s = result1.getString("about");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return s;
    }

    public static ObjectNakaz getNakaz(int idX) {
        Connection connection = getConnection();
        ObjectNakaz nakaz = null;
        try {

            PreparedStatement preparedStatement;

            String query = "SELECT nakaz.id, nakaz.about, nakaz.nom_nakaz, nakaz.date_nakaz, nakaz_type.value " + "\n" +
                    "FROM nakaz JOIN nakaz_type ON nakaz.type = nakaz_type.type WHERE id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            java.util.Date date;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");

            while (result1.next()) {

                date = originalFormat.parse(result1.getDate("date_nakaz").toString());
                date = targetFormat.parse(targetFormat.format(date));

                nakaz = new ObjectNakaz(result1.getInt("id"), result1.getString("nom_nakaz"),
                        date, result1.getString("value"));
                nakaz.setAbout(result1.getString("about"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return nakaz;
    }

    public static ObservableList<ObjectNakaz> getNakazByType(int type) {
        Connection connection = getConnection();
        ObservableList<ObjectNakaz> list = FXCollections.observableArrayList();
        ObjectNakaz o = null;
        try {

            PreparedStatement preparedStatement;

            String query = "SELECT nakaz.id, nakaz.about, nakaz.nom_nakaz, nakaz.date_nakaz FROM nakaz WHERE nakaz.type =?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, type);
            ResultSet result1 = preparedStatement.executeQuery();

            java.util.Date date;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");

            while (result1.next()) {

                date = originalFormat.parse(result1.getDate("date_nakaz").toString());
                date = targetFormat.parse(targetFormat.format(date));

                o = new ObjectNakaz(result1.getInt("id"), result1.getString("nom_nakaz"), date, null);
                list.add(o);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static void addNewNakaz(String newNumber, String about, int type, LocalDate date) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            LocalDateTime ldt = LocalDateTime.of(date, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            String query = "INSERT INTO nakaz (about,nom_nakaz,date_nakaz,type) VALUES (?,?,?,?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, about);
            preparedStatement.setString(2, newNumber);
            preparedStatement.setDate(3, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setInt(4, type);
            preparedStatement.execute();

            writeLog("INSERT INTO nakaz (about,nom_nakaz,date_nakaz,type) VALUES ('" + about + "','" + newNumber + "','" + date + "'," + type + ")");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void editNakaz(String newNumber, String about, int type, LocalDate date, int id) {


        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;


            LocalDateTime ldt = LocalDateTime.of(date, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            String query = "UPDATE nakaz SET about = ?,nom_nakaz=?,date_nakaz=? ,type=?  WHERE id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, about);
            preparedStatement.setString(2, newNumber);
            preparedStatement.setDate(3, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setInt(4, type);
            preparedStatement.setInt(5, id);
            preparedStatement.execute();

            writeLog("UPDATE nakaz SET about = '" + about + "',nom_nakaz='" + newNumber + "',date_nakaz='" + date + "' ,type=" + type + "  WHERE id=" + id);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    /************************************************
     VYKLADACH BLOCK
     ************************************************/

    public static ObservableList<ObjectVykladach> getAllVykladachiBySpeciality(int idX) {
        Connection connection = getConnection();
        ObservableList<ObjectVykladach> list = FXCollections.observableArrayList();
        try {

            PreparedStatement preparedStatement;
            String query = "SELECT name,id FROM vykladachi " +
                    "JOIN kerivnik_speciality ON kerivnik_speciality.id_nav_kerivnik = vykladachi.id WHERE id_speciality=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();
            while (result1.next()) {
                ObjectVykladach o = new ObjectVykladach(result1.getInt("id"), result1.getString("name"));
                list.add(o);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static ObservableList<ObjectVykladach> getAllVykladachi() {
        Connection connection = getConnection();
        ObservableList<ObjectVykladach> list = FXCollections.observableArrayList();
        ObjectVykladach o = null;
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT * FROM vykladachi");

            while (result1.next()) {
                o = new ObjectVykladach(result1.getInt("id"), result1.getString("name"));
                o.setSpecialities(getSpecialityByNavKerivnik(result1.getInt("id")));
                list.add(o);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static void addNewVykladach(String nameVykladach) {
        Connection connection = getConnection();


        try {

            PreparedStatement preparedStatement;
            String query = "INSERT INTO vykladachi(name) VALUES(?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameVykladach);
            preparedStatement.execute();
            connection.close();


            writeLog("INSERT INTO vykladachi(name) VALUES('" + nameVykladach + "')");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    public static void editVykladach(int id, String newName, ObservableList<ObjectSpeciality> specialities) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "UPDATE vykladachi SET name = ? WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();

            writeLog("UPDATE vykladachi SET name = '" + newName + "' WHERE id=" + id);
            setSpecialitiesVykladach(id, specialities);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }

    }

    private static void setSpecialitiesVykladach(int id, ObservableList<ObjectSpeciality> specialities) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "DELETE FROM kerivnik_speciality WHERE id_nav_kerivnik=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            writeLog("DELETE FROM kerivnik_speciality WHERE id_nav_kerivnik=" + id);


            query = "INSERT INTO kerivnik_speciality (id_nav_kerivnik,id_speciality) VALUES (?,?)";
            preparedStatement = connection.prepareStatement(query);

            for (ObjectSpeciality s : specialities) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, s.getId());
                preparedStatement.execute();
                writeLog("INSERT INTO kerivnik_speciality (id_nav_kerivnik,id_speciality) VALUES (" + id + "," + s.getId() + ")");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    public static ObservableList<ObjectSpeciality> getSpecialityByNavKerivnik(int idKerivnik) {

        Connection connection = getConnection();
        ObservableList<ObjectSpeciality> list = FXCollections.observableArrayList();
        ObjectSpeciality o = null;
        try {

            PreparedStatement preparedStatement;
            String query = "SELECT name,id,code,status,faculty_id FROM specialities " +
                    "JOIN kerivnik_speciality id_speciality ON specialities.id = id_speciality.id_speciality WHERE id_nav_kerivnik=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idKerivnik);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                o = new ObjectSpeciality(result1.getInt("id"), result1.getString("name"),
                        result1.getString("status"), result1.getString("code"));

                int facultyId = -1;

                try {
                    facultyId = result1.getInt("faculty_id");
                } catch (Exception e) {

                }


                if (facultyId != -1) o.setFaculty(getFaculty(facultyId));
                else o.setFaculty(null);

                ObjectKafedra kafedra = getKafedra(result1.getInt("id"));
                if (kafedra != null) o.setKafedra(kafedra);
                else o.setKafedra(null);

                o.setKerivniki_id(getAllKerivniksOfSpeciality(o.getId()));

                list.add(o);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    /************************************************
     NAKAZ TYPE BLOCK
     ************************************************/

    public static ObservableList<ObjectTypeNakazi> getAllTypeNakazi() {
        Connection connection = getConnection();
        ObservableList<ObjectTypeNakazi> list = FXCollections.observableArrayList();
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT * FROM nakaz_type");

            while (result1.next()) {

                list.add(new ObjectTypeNakazi(result1.getInt("type"), result1.getString("value")));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }

    public static void addNewNakazType(String valueType) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "INSERT INTO nakaz_type(value) VALUES(?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, valueType);
            preparedStatement.execute();

            writeLog("INSERT INTO nakaz_type(value) VALUES('" + valueType + "')");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    /************************************************
     SPECIALITY BLOCK
     ************************************************/

    public static ObservableList<ObjectSpeciality> getAllSpeciality() {
        Connection connection = getConnection();
        ObservableList<ObjectSpeciality> list = FXCollections.observableArrayList();
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT id, name, status, code,faculty_id FROM specialities");
            while (result1.next()) {
                ObjectSpeciality o = new ObjectSpeciality(result1.getInt("id"), result1.getString("name"),
                        result1.getString("status"), result1.getString("code"));

                int facultyId = -1;

                try {
                    facultyId = result1.getInt("faculty_id");
                } catch (Exception e) {

                }


                if (facultyId != -1) o.setFaculty(getFaculty(facultyId));
                else o.setFaculty(null);

                ObjectKafedra kafedra = getKafedra(result1.getInt("id"));
                if (kafedra != null) o.setKafedra(kafedra);
                else o.setKafedra(null);

                o.setKerivniki_id(getAllKerivniksOfSpeciality(o.getId()));
                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }

    public static ArrayList<Integer> getAllKerivniksOfSpeciality(int idX) {

        Connection connection = getConnection();
        ArrayList<Integer> list = new ArrayList<>();

        try {

            PreparedStatement preparedStatement;
            String query = "SELECT id_nav_kerivnik FROM kerivnik_speciality WHERE id_speciality=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                list.add(result1.getInt("id_nav_kerivnik"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static void addNewSpeciality(String nameX, String codeX, int faculty_idX, int kafedra_idX) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;
            String query;
            if (faculty_idX != -1)
                query = "INSERT INTO specialities (name,code,status,faculty_id) VALUES (?,?,'Активно',?)";
            else query = "INSERT INTO specialities (name,code,status) VALUES (?,?,'Активно')";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setString(2, codeX);
            if (faculty_idX != -1) preparedStatement.setInt(3, faculty_idX);
            preparedStatement.execute();

            if (faculty_idX != -1)
                writeLog("INSERT INTO specialities (name,code,status,faculty_id) VALUES ('" + nameX + "','" + codeX + "','Активно'," + faculty_idX + ")");
            else
                writeLog("INSERT INTO specialities (name,code,status) VALUES ('" + nameX + "','" + codeX + "','Активно')");


            if (kafedra_idX != -1) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM specialities");
                int idX = 0;
                while (resultSet.next()) {
                    idX = resultSet.getInt(1);
                }

                PreparedStatement preparedStatement2;
                String query2 = "UPDATE kafedra SET speciality_id = ? WHERE id=?";
                preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setInt(1, idX);
                preparedStatement2.setInt(2, kafedra_idX);
                preparedStatement2.execute();


                writeLog("UPDATE kafedra SET speciality_id = " + idX + " WHERE id=" + kafedra_idX);


            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void editSpeciality(int idX, String nameX, String codex, String statusX, int faculty_idX, int kafedra_idX) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;


            String query = "UPDATE specialities SET name= ?,code=?,status=?,faculty_id=? WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setString(2, codex);
            preparedStatement.setString(3, statusX);

            if (faculty_idX == -1) {
                preparedStatement.setInt(4, NULL);
            } else {
                preparedStatement.setInt(4, faculty_idX);
            }
            preparedStatement.setInt(5, idX);

            if (faculty_idX == 1)
                writeLog("UPDATE specialities SET name= '" + nameX + "',code='" + codex + "',status='" + statusX + "',faculty_id=NULL WHERE id=" + idX);
            else
                writeLog("UPDATE specialities SET name= '" + nameX + "',code='" + codex + "',status='" + statusX + "',faculty_id='" + faculty_idX + "' WHERE id=" + idX);
            preparedStatement.execute();


            if (kafedra_idX != -1) {

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select id from kafedra WHERE speciality_id=" + idX);


                int idOld = 0;

                while (resultSet.next()) {
                    idOld = resultSet.getInt(1);
                }


                if (idOld != 0) {
                    query = "UPDATE kafedra SET speciality_id=NULL WHERE id=?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, idOld);
                    preparedStatement.execute();
                    writeLog("UPDATE kafedra SET speciality_id=NULL WHERE id=" + idOld);
                }

                if (kafedra_idX != -2) {
                    query = "UPDATE kafedra SET speciality_id= ? WHERE id=?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, idX);
                    preparedStatement.setInt(2, kafedra_idX);
                    preparedStatement.execute();

                    writeLog("UPDATE kafedra SET speciality_id=" + idX + " WHERE id=" + kafedra_idX);
                }
            } else {


            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    /************************************************
     KAFEDRA BLOCK
     ************************************************/

    public static ObservableList<ObjectKafedra> getAllKafedras() {
        Connection connection = getConnection();
        ObservableList<ObjectKafedra> list = FXCollections.observableArrayList();
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT kafedra.id, kafedra.name, kafedra.room, specialities.name FROM kafedra LEFT OUTER JOIN  specialities  ON kafedra.speciality_id = specialities.id");

            while (result1.next()) {
                ObjectKafedra o = new ObjectKafedra(result1.getInt(1), result1.getString(2), result1.getString(3));
                String s = result1.getString(4);
                if (s != null) o.setSpeciality(s);
                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static ObjectKafedra getKafedra(int speciality_idX) {

        Connection connection = getConnection();
        ObjectKafedra o = null;
        try {

            PreparedStatement preparedStatement;
            String query = "SELECT name,id,room FROM kafedra WHERE speciality_id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, speciality_idX);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                o = new ObjectKafedra(result1.getInt("id"), result1.getString("room"), result1.getString("name"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return o;
    }


    public static void addNewKafedra(String nameX, String roomX) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "INSERT INTO kafedra(name,room) VALUES(?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setString(2, roomX);
            preparedStatement.execute();


            writeLog("INSERT INTO kafedra(name,room) VALUES('" + nameX + "','" + roomX + "')");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    public static void editKafedra(int id, String nameX, String roomX) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "UPDATE kafedra SET name= ?,room=? WHERE id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setString(2, roomX);
            preparedStatement.setInt(3, id);
            preparedStatement.execute();

            writeLog("UPDATE kafedra SET name= '" + nameX + "',room='" + roomX + "' WHERE id=" + id);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    /************************************************s
     DISCIPLINE BLOCK
     ************************************************/


    public static void addNewDiscipline(String nameX, int idVykl, int idType, int nomSymestr, int idSpeciality, int nomCourse, int form) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "INSERT INTO disciplines (name,semestr,status,control_type,vykladach_id,course,form) VALUES (?,?,'Активно',?,?,?,?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setInt(2, nomSymestr);
            preparedStatement.setInt(3, idType);
            preparedStatement.setInt(4, idVykl);
            preparedStatement.setInt(5, nomCourse);
            preparedStatement.setInt(6, form);
            preparedStatement.execute();


            writeLog("INSERT INTO disciplines (name,semestr,status,control_type,vykladach_id,course,form) VALUES ('" + nameX + "'," + nomSymestr + ",'Активно'," + idType + "," + idVykl + "," + nomCourse + "," + form + ")");


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM disciplines");
            int idDiscipline = 0;
            while (resultSet.next()) {
                idDiscipline = resultSet.getInt(1);
            }


            String query2 = "INSERT INTO speciality_disciplines (id_speciality, id_disciplines) VALUES (?,?)";
            PreparedStatement preparedStatement2;
            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setInt(1, idSpeciality);
            preparedStatement2.setInt(2, idDiscipline);
            preparedStatement2.execute();

            writeLog("INSERT INTO speciality_disciplines (id_speciality, id_disciplines) VALUES (" + idSpeciality + "," + idDiscipline + ")");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void editDiscipline(String nameX, int idVykl, int idType, int nomSymestr, int idSpeciality, int id, String statusX, int courseX, int formX) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "UPDATE disciplines SET name=?,status=?,semestr=? ,control_type=?,vykladach_id=?, course=? , form =? WHERE id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setString(2, statusX);
            preparedStatement.setInt(3, nomSymestr);
            preparedStatement.setInt(4, idType);
            preparedStatement.setInt(5, idVykl);
            preparedStatement.setInt(6, courseX);
            preparedStatement.setInt(7, formX);
            preparedStatement.setInt(8, id);
            preparedStatement.execute();


            writeLog("UPDATE disciplines SET name='" + nameX + "',status='" + statusX + "',semestr=" + nomSymestr + " ,control_type=" + idType + ",vykladach_id=" + idVykl + ",course=" + courseX + ",form=" + formX + "  WHERE id=" + id);


            PreparedStatement preparedStatement2;

            String query2 = "UPDATE speciality_disciplines SET id_speciality=?  WHERE id_disciplines=?";

            preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setInt(1, idSpeciality);
            preparedStatement2.setInt(2, id);
            preparedStatement2.execute();

            writeLog("UPDATE speciality_disciplines SET id_speciality='" + idSpeciality + "'  WHERE id_disciplines=" + id);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static ObservableList<ObjectDiscipline> getAllDisciplines(int form) {
        Connection connection = getConnection();
        ObservableList<ObjectDiscipline> list = FXCollections.observableArrayList();
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT disciplines.name,disciplines.id, disciplines.status, disciplines.semestr, vykladachi.name, discipline_type.type, disciplines.course, disciplines.form " +
                    "FROM disciplines INNER JOIN vykladachi ON disciplines.vykladach_id = vykladachi.id INNER JOIN discipline_type ON disciplines.control_type = discipline_type.id   " + ((form != 0) ? "WHERE disciplines.form=" + form : "") + " ORDER BY disciplines.id asc");


            while (result1.next()) {
                ObjectDiscipline o = new ObjectDiscipline(result1.getInt("id"),
                        result1.getString("name"), result1.getString("status"),
                        result1.getInt("semestr"), result1.getString(6), result1.getInt("course"));
                o.setVykladach(result1.getString(5));
                o.setSpeciality(getSpecialityByDisciplineId(result1.getInt("id")).getName());
                o.setFormValue(result1.getInt(8));
                list.add(o);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static ObjectSpeciality getSpecialityByDisciplineId(int discipline_idX) {

        Connection connection = getConnection();
        ObjectSpeciality o = null;
        try {

            PreparedStatement preparedStatement;
            String query = "SELECT name,id,code FROM specialities " +
                    "JOIN speciality_disciplines discipline ON specialities.id = discipline.id_speciality WHERE id_disciplines=?";


            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, discipline_idX);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                o = new ObjectSpeciality(result1.getInt("id"), result1.getString("name"), result1.getString("code"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return o;
    }


    /************************************************
     DISCIPLINE TYPE BLOCK
     ************************************************/

    public static ObservableList<ObjectTypeDiscipl> getAllTypeDiscipline() {
        Connection connection = getConnection();
        ObservableList<ObjectTypeDiscipl> list = FXCollections.observableArrayList();
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT * FROM discipline_type");

            while (result1.next()) {
                list.add(new ObjectTypeDiscipl(result1.getInt("id"), result1.getString("type")));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static void addNewDisciplineType(String valueType) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "INSERT INTO discipline_type(type) VALUES(?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, valueType);
            preparedStatement.execute();

            writeLog("INSERT INTO discipline_type(type) VALUES('" + valueType + "')");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    /***********************************************
     ASPIRANT BLOCK
     ************************************************/

    public static void addNewAspirant(String pib, LocalDate birthdayData, int idKerivnik, int idSpecialilty, int idContacts, int idPassport, int idStatus, int year, int male, short form, FileInputStream fis, long fileLenght) {
        Connection connection = getConnection();
        try {

            LocalDateTime ldt = LocalDateTime.of(birthdayData, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
            PreparedStatement preparedStatement;

            String query = "INSERT INTO aspirant(name,birthday,id_passport,id_nav_kerivnik,id_speciality,id_contacts,id_status,year,is_male,study_form,photo) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pib);
            preparedStatement.setDate(2, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setInt(3, idPassport);
            preparedStatement.setInt(4, idKerivnik);
            preparedStatement.setInt(5, idSpecialilty);
            preparedStatement.setInt(6, idContacts);
            preparedStatement.setInt(7, idStatus);
            preparedStatement.setInt(8, year);
            preparedStatement.setInt(9, male);
            preparedStatement.setInt(10, form);
            if (fis == null) preparedStatement.setBinaryStream(11, null, 0);
            else preparedStatement.setBinaryStream(11, fis, (int) fileLenght);
            preparedStatement.execute();

            writeLog("INSERT INTO aspirant(name,birthday,id_passport,id_nav_kerivnik,id_speciality,id_contacts,id_status,year,is_male,study_form) " +
                    "VALUES('" + pib + "','" + birthdayData + "'," + idPassport + "," + idKerivnik + "," + idSpecialilty + "," + idContacts + "," + idStatus + "," + year + "," + male + "," + form + ")");


            int idAspirant = -1;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM aspirant");
            while (resultSet.next()) {
                idAspirant = resultSet.getInt(1);
            }


            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT disciplines.name, disciplines.control_type, disciplines.course, disciplines.semestr, vykladachi.name, disciplines.id FROM disciplines INNER JOIN vykladachi on vykladachi.id = disciplines.vykladach_id INNER JOIN speciality_disciplines  ON disciplines.id = speciality_disciplines.id_disciplines WHERE disciplines.status='Активно' AND disciplines.form=" + form + " AND speciality_disciplines.id_speciality=" + idSpecialilty);


            while (resultSet.next()) {

                String disName = resultSet.getString(1);
                String vyklName = resultSet.getString(5);
                int controlType = resultSet.getInt(2);
                int course = resultSet.getInt(3);
                int semestr = resultSet.getInt(4);
                int disciplId = resultSet.getInt(6);

                query = "INSERT INTO results(name_discipline,name_vykl,discipline_type,course,semestr,aspirant_id,discipline_id) VALUES(?,?,?,?,?,?,?)";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, disName);
                preparedStatement.setString(2, vyklName);
                preparedStatement.setInt(3, controlType);
                preparedStatement.setInt(4, course);
                preparedStatement.setInt(5, semestr);
                preparedStatement.setInt(6, idAspirant);
                preparedStatement.setInt(7, disciplId);

                preparedStatement.execute();
                writeLog("INSERT INTO results(name_discipline,name_vykl,discipline_type,course,semestr,aspirant_id,discipline_id)  VALUES('" + disName + "','" + vyklName + "', '" + controlType + "', " + course + ", " + semestr + "," + idAspirant + "," + disciplId + ")");

            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    public static int addNewPassportData(String nameData, String seriaData, String numberData, String publishedBy, LocalDate datePublished, String identificatorData) {
        int id = -1;

        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;


            LocalDateTime ldt = LocalDateTime.of(datePublished, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            String query = "INSERT INTO passport(name,seria,number,published_by,published_date,identificator) VALUES(?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameData);
            preparedStatement.setString(2, seriaData);
            preparedStatement.setString(3, numberData);
            preparedStatement.setString(4, publishedBy);
            preparedStatement.setDate(5, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setString(6, identificatorData);
            preparedStatement.execute();

            writeLog("INSERT INTO passport(name,seria,number,published_by,published_date,identificator)" +
                    "VALUES('" + nameData + "','" + seriaData + "','" + numberData + "','" + publishedBy + "','" + datePublished + "','" + identificatorData + "')");


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM passport");
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return id;
    }

    public static int getLastApirantId() {
        int idAspirant = -1;
        Connection connection = getConnection();
        try {


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM aspirant");
            while (resultSet.next()) {
                idAspirant = resultSet.getInt(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return idAspirant;
    }


    public static ObservableList<ObjectStudyForm> getAllForms() {

        Connection connection = getConnection();
        ObservableList<ObjectStudyForm> list = FXCollections.observableArrayList();

        try {

            PreparedStatement preparedStatement;
            String query = "SELECT id,form FROM study_form ORDER BY id ASC";


            preparedStatement = connection.prepareStatement(query);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                list.add(new ObjectStudyForm(result1.getInt("id"), result1.getString("form")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static int addStatusData(LocalDate dateX, int idNakaz, int idAspirant) {
        int statusId = -1;

        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;


            LocalDateTime ldt = LocalDateTime.of(dateX, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            String query = "INSERT INTO status(date,aspirant_id,id_nakaz,madeBy) VALUES(?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setInt(2, idAspirant);
            preparedStatement.setInt(3, idNakaz);
            preparedStatement.setString(4, ControllerLogin.login);
            preparedStatement.execute();

            writeLog("INSERT INTO status(date,aspirant_id,id_nakaz,madeBy) VALUES('" + dateX + "'," + idNakaz + "," + idAspirant + ", '" + ControllerLogin.login + "')");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM status");
            while (resultSet.next()) {
                statusId = resultSet.getInt(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return statusId;
    }

    public static int addNewContactsData(String telephoneData, String emailData, String addressData) {
        int id = -1;

        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "INSERT INTO contacts(phone,email,address) VALUES(?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, telephoneData);
            preparedStatement.setString(2, emailData);
            preparedStatement.setString(3, addressData);
            preparedStatement.execute();

            writeLog("INSERT INTO contacts(phone,email,address) VALUES('" + telephoneData + "','" + emailData + "','" + addressData + "')");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM contacts");
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return id;
    }

    public static ObservableList<ObjectAspirant> getAllAspirants() {
        Connection connection = getConnection();
        ObservableList<ObjectAspirant> list = FXCollections.observableArrayList();
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT  aspirant.id,aspirant.name, aspirant.birthday, vykladachi.name, specialities.name, aspirant.id_status,aspirant.notes, kafedra.room, aspirant.year, sf.form, aspirant.is_male " +
                    "FROM aspirant " +
                    "INNER JOIN  vykladachi ON aspirant.id_nav_kerivnik = vykladachi.id " +
                    "INNER JOIN specialities ON specialities.id = aspirant.id_speciality  " +
                    "INNER JOIN kafedra ON kafedra.speciality_id = aspirant.id_speciality  " +
                    "INNER JOIN study_form sf ON aspirant.study_form = sf.id WHERE aspirant.status!=-1" +
                    " ORDER BY aspirant.id ASC");


            java.util.Date date;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");


            while (result1.next()) {

                date = originalFormat.parse(result1.getDate(3).toString());
                date = targetFormat.parse(targetFormat.format(date));
                ObjectAspirant o = new ObjectAspirant(result1.getInt(1), result1.getString(2), result1.getString(5),
                        date, result1.getString(4), result1.getInt(6), result1.getInt(9));
                o.setKafedraKabinet(result1.getString(8));
                o.setNote(result1.getString(7));

                o.setForm(result1.getString(10));
                o.setMale(result1.getInt(11) == 1 ? "Чоловіча" : "Жіноча");

                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;

    }

    public static void addStatus(ObjectAspirant o, LocalDate dateX, int aspirantId, int nakazId) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;


            LocalDateTime ldt = LocalDateTime.of(dateX, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            String query = "INSERT INTO status(aspirant_id,id_nakaz,date,madeBy) VALUES(?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, aspirantId);
            preparedStatement.setInt(2, nakazId);
            preparedStatement.setDate(3, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setString(4, ControllerLogin.login);
            preparedStatement.execute();


            writeLog("INSERT INTO status(aspirant_id,id_nakaz,date,madeBy) VALUES(" + aspirantId + "," + nakazId + ",'" + dateX + "','" + ControllerLogin.login + "')");

            int maxStatusId = -1;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT max(id) FROM status");

            while (resultSet.next()) {
                maxStatusId = resultSet.getInt(1);
            }


            query = "UPDATE aspirant SET id_status= ? WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, maxStatusId);
            preparedStatement.setInt(2, aspirantId);
            preparedStatement.execute();
            writeLog("UPDATE aspirant SET id_status=" + maxStatusId + " WHERE id=" + aspirantId);

            o.setStatusId(maxStatusId);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static String getTypeByNakaz(int idX) {

        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;

            String query = ("SELECT id_nakaz FROM status WHERE id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            int nakaz = -1;
            while (result1.next()) {
                nakaz = result1.getInt(1);
            }


            query = "SELECT nakaz_type.value FROM nakaz INNER JOIN nakaz_type ON nakaz_type.type=nakaz.type WHERE nakaz.id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, nakaz);
            ResultSet result2 = preparedStatement.executeQuery();


            while (result2.next()) {
                return result2.getString(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return null;
    }


    public static ObjectContacts getContactsByAspirant(int idX) {

        ObjectContacts o = null;
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT aspirant.id_contacts, contacts.email,contacts.phone,contacts.address FROM aspirant INNER JOIN  contacts ON aspirant.id_contacts = contacts.id WHERE aspirant.id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                o = new ObjectContacts(result1.getString("phone"), result1.getString("email"), result1.getString("address"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return o;
    }


    public static ObjectPassport getPassportByAspirant(int idX) {
        ObjectPassport o = null;

        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT aspirant.id_passport, passport.seria,passport.number,passport.identificator, passport.published_by, passport.published_date FROM aspirant INNER JOIN  passport ON aspirant.id_passport = passport.id WHERE aspirant.id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();


            java.util.Date date;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");


            while (result1.next()) {

                date = originalFormat.parse(result1.getDate("published_date").toString());
                date = targetFormat.parse(targetFormat.format(date));
                LocalDate dateLocal = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                o = new ObjectPassport(result1.getString("seria"), result1.getString("number"), result1.getString("published_by"), dateLocal, result1.getString("identificator"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return o;
    }


    public static InputStream getAspirantPhoto(int idX) {
        InputStream o = null;

        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT photo FROM aspirant WHERE aspirant.id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();


            while (result1.next()) {
                o = result1.getBinaryStream(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return o;
    }

    public static ObjectNakaz getNakazZarahByAspirantId(int idX) {

        ObjectNakaz o = null;
        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT id_nakaz FROM status INNER JOIN  nakaz ON nakaz.id = status.id_nakaz WHERE nakaz.type=4 AND aspirant_id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                return getNakaz(result1.getInt(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return null;
    }

    public static void deleteAspirant(int idX) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query;
            query = "UPDATE aspirant SET status=-1 WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            preparedStatement.execute();
            writeLog(String.format("UPDATE aspirant status =-1  WHERE aspirant_id=%d", idX));


        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void editAspirant(int idX, String nameX, LocalDate date, int kerivnik, int idSpecialilty, int idNakaz, int year, int male, short form, FileInputStream fis, long fileLenght) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            LocalDateTime ldt = LocalDateTime.of(date, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            String query;
            if (fis != null) {
                query = "UPDATE aspirant SET name=?, birthday=?, id_nav_kerivnik=?, id_speciality=?,year=?, is_male=?,study_form=?,photo=? WHERE id=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nameX);
                preparedStatement.setDate(2, new java.sql.Date(zdt.toInstant().toEpochMilli()));
                preparedStatement.setInt(3, kerivnik);
                preparedStatement.setInt(4, idSpecialilty);
                preparedStatement.setInt(5, year);
                preparedStatement.setInt(6, male);
                preparedStatement.setInt(7, form);
                preparedStatement.setBinaryStream(8, fis, (int) fileLenght);
                preparedStatement.setInt(9, idX);
            } else {
                query = "UPDATE aspirant SET name=?, birthday=?, id_nav_kerivnik=?, id_speciality=?,year=?, is_male=?,study_form=? WHERE id=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nameX);
                preparedStatement.setDate(2, new java.sql.Date(zdt.toInstant().toEpochMilli()));
                preparedStatement.setInt(3, kerivnik);
                preparedStatement.setInt(4, idSpecialilty);
                preparedStatement.setInt(5, year);
                preparedStatement.setInt(6, male);
                preparedStatement.setInt(7, form);
                preparedStatement.setInt(8, idX);
            }


            preparedStatement.execute();

            writeLog(String.format("UPDATE aspirant SET name=%s, birthday='%s', id_nav_kerivnik=%d, id_speciality=%d, year=%d, male=%d,form=%d WHERE id=%d", nameX, date, kerivnik, idSpecialilty, year, male, form, idX));

            query = ("UPDATE status r JOIN nakaz n ON n.id=r.id_nakaz  SET id_nakaz =?  WHERE (aspirant_id=? AND n.type=4) ");

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idNakaz);
            preparedStatement.setInt(2, idX);
            preparedStatement.execute();

            writeLog(String.format("UPDATE status r JOIN nakaz n on n.id=r.id_nakaz  SET id_nakaz =%d  WHERE (aspirant_id=%d AND n.type=4)", idNakaz, idX));


        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void editNote(int aspirantId, String text) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = "UPDATE aspirant SET notes= ? WHERE id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, aspirantId);

            preparedStatement.execute();

            writeLog("UPDATE aspirant SET notes = '" + text + "' WHERE id=" + aspirantId);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void editMark(int id, String markPo, String markNa, String markIE) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;
            String query = "UPDATE results SET mark_points= ?,mark_national= ?,mark_ects= ? WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, markPo);
            preparedStatement.setString(2, markNa);
            preparedStatement.setString(3, markIE);
            preparedStatement.setInt(4, id);
            preparedStatement.execute();
            writeLog("UPDATE results SET mark_points = " + markPo + ",mark_national = '" + markNa + "', mark_ects='" + markIE + "' WHERE id=" + id);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void editPassportData(int idX, String nameX, String seriaData, String numberData, String publishedByData, LocalDate datePublishedData, int identificatorData) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = ("SELECT id_passport FROM aspirant WHERE id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            int idP = 0;
            while (result1.next()) {
                idP = result1.getInt(1);
            }


            LocalDateTime ldt = LocalDateTime.of(datePublishedData, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            query = "UPDATE passport SET name=?,seria=?, number=?, published_by=?,published_date=?,identificator=? WHERE id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setString(2, seriaData);
            preparedStatement.setString(3, numberData);
            preparedStatement.setString(4, publishedByData);
            preparedStatement.setDate(5, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setInt(6, identificatorData);
            preparedStatement.setInt(7, idP);
            preparedStatement.execute();

            writeLog(String.format("UPDATE passport SET name=%s,seria=%s, number=%s, published_by=%s,published_date='%s',identificator=%d WHERE id=%d", nameX, seriaData, numberData, publishedByData, datePublishedData, identificatorData, idP));
        } catch (Exception e) {

        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void editContacts(int idX, String telephoneData, String emailData, String addressData) {
        Connection connection = getConnection();

        try {

            PreparedStatement preparedStatement;

            String query = ("SELECT id_contacts FROM aspirant WHERE id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();

            int idP = 0;
            while (result1.next()) {
                idP = result1.getInt(1);
            }


            query = "UPDATE contacts SET phone=?,address=?, email=? WHERE id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, telephoneData);
            preparedStatement.setString(2, addressData);
            preparedStatement.setString(3, emailData);
            preparedStatement.setInt(4, idP);
            preparedStatement.execute();


            writeLog(String.format("UPDATE contacts SET phone=%s,address=%s, email=%s WHERE id=%s", telephoneData, addressData, emailData, idP));
        } catch (Exception e) {

        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    public static ArrayList<ObjectHistoryAspirant> getAspirantHistory(int idX) {
        Connection connection = getConnection();
        ArrayList<ObjectHistoryAspirant> list = new ArrayList<>();

        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT status.date, status.madeBy, nn.value,n.nom_nakaz,n.date_nakaz  FROM status INNER JOIN  nakaz n ON status.id_nakaz = n.id INNER JOIN  nakaz_type nn ON n.type = nn.type WHERE status.aspirant_id=? ORDER BY  status.id DESC ");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();


            java.util.Date date;
            java.util.Date dateNakaz;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");


            while (result1.next()) {
                date = originalFormat.parse(result1.getDate(1).toString());
                date = targetFormat.parse(targetFormat.format(date));

                dateNakaz = originalFormat.parse(result1.getDate(5).toString());
                dateNakaz = targetFormat.parse(targetFormat.format(dateNakaz));


                list.add(new ObjectHistoryAspirant(date, result1.getString(2), result1.getString(3), result1.getString(4), dateNakaz));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static ObservableList<ObjectResult> getAllResultsByApirantId(int aspirantId) {
        Connection connection = getConnection();
        ObservableList<ObjectResult> list = FXCollections.observableArrayList();

        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT name_discipline,name_vykl,discipline_type.type,course,semestr,results.id,mark_points,mark_ects,mark_national FROM results INNER JOIN discipline_type ON discipline_type.id = results.discipline_type WHERE aspirant_id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, aspirantId);
            ResultSet result1 = preparedStatement.executeQuery();


            while (result1.next()) {
                list.add(new ObjectResult(result1.getString(1), result1.getString(2), result1.getString(3), result1.getString(9), result1.getString(8), result1.getString(7), result1.getInt(4), result1.getInt(5), result1.getInt(6)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }

    public static void editScienceWork(String nameX, String where, String link, LocalDate date, int workId) {
        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;

            LocalDateTime ldt = LocalDateTime.of(date, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            String query = "UPDATE science_work SET name=?,`where`=?, link=?, date=? WHERE id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setString(2, where);
            preparedStatement.setString(3, link);
            preparedStatement.setDate(4, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setInt(5, workId);
            preparedStatement.execute();


            writeLog(String.format(" UPDATE science_work SET name=%s,`where`=%s, link=%s, date='%s' WHERE id=%d", nameX, where, link, date, workId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }


    public static void addScienceWork(String nameX, String where, String link, LocalDate date, int aspirantId) {
        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;


            LocalDateTime ldt = LocalDateTime.of(date, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));

            String query = "INSERT INTO science_work(name,`where`,link,date,aspirant_id) VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameX);
            preparedStatement.setString(2, where);
            preparedStatement.setString(3, link);
            preparedStatement.setDate(4, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setInt(5, aspirantId);
            preparedStatement.execute();


            writeLog("INSERT INTO science_work(name,`where`,link,date,aspirant_id) VALUES('" + nameX + "','" + where + "','" + link + "','" + new java.sql.Date(zdt.toInstant().toEpochMilli()) + "'," + aspirantId + ")");


        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
        }
    }

    public static ArrayList<ObjectScienceWork> getAspirantScienceWorks(int idX) {
        Connection connection = getConnection();
        ArrayList<ObjectScienceWork> list = new ArrayList<>();

        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT science_work.name, science_work.where,science_work.date,science_work.link,science_work.id  FROM science_work WHERE aspirant_id=? ORDER BY  id DESC ");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();


            java.util.Date date;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");


            while (result1.next()) {

                date = originalFormat.parse(result1.getDate(3).toString());
                date = targetFormat.parse(targetFormat.format(date));

                list.add(new ObjectScienceWork(result1.getInt(5), date, result1.getString(2), result1.getString(1), result1.getString(4)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }

    public static ObjectScienceWork getAspirantScienceWork(int idX) {
        Connection connection = getConnection();
        ObjectScienceWork obj = null;

        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT science_work.name, science_work.where,science_work.date,science_work.link,science_work.id  FROM science_work WHERE science_work.id=? ");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idX);
            ResultSet result1 = preparedStatement.executeQuery();


            java.util.Date date;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");


            while (result1.next()) {

                date = originalFormat.parse(result1.getDate(3).toString());
                date = targetFormat.parse(targetFormat.format(date));

                obj = new ObjectScienceWork(result1.getInt(5), date, result1.getString(2), result1.getString(1), result1.getString(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return obj;
    }

    public static ArrayList<ObjectPractice> getAspirantPractice(int aspirantId) {
        Connection connection = getConnection();
        ArrayList<ObjectPractice> list = new ArrayList<>();

        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT name_practice,organization,mark_national_scale,mark_ectis,mark_points,credits,work_description,commission_names,started,ended,id FROM practics WHERE aspirant_id=? ORDER BY id DESC ");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, aspirantId);
            ResultSet result1 = preparedStatement.executeQuery();


            java.util.Date dateFrom;
            java.util.Date dateTill;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");


            while (result1.next()) {

                dateFrom = targetFormat.parse(targetFormat.format(originalFormat.parse(result1.getDate(9).toString())));
                dateTill = targetFormat.parse(targetFormat.format(originalFormat.parse(result1.getDate(10).toString())));


                list.add(new ObjectPractice(result1.getString(1), result1.getString(2), dateFrom, dateTill, result1.getString(3),
                        result1.getString(4), result1.getInt(5), result1.getString(6),
                        result1.getString(7), result1.getString(8), result1.getInt(11)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;
    }


    public static void editPractice(String name_, String organization_, String markNational_, String markECTIS_, int markPoints_, String credits_, LocalDate dateFrom_, LocalDate dateTill_, String workDescription_, String namesCommission_, int workId) {
        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;

            String query = "UPDATE practics SET name_practice=?,organization=?,mark_national_scale=?,mark_ectis=?,mark_points=?,credits=?,work_description=?," +
                    "commission_names=?,started=?,ended=? WHERE id=?";


            LocalDateTime ldt = LocalDateTime.of(dateFrom_, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));


            LocalDateTime ldt2 = LocalDateTime.of(dateTill_, LocalTime.of(16, 0));
            ZonedDateTime zdt2 = ldt2.atZone(ZoneId.of("America/Los_Angeles"));


            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name_);
            preparedStatement.setString(2, organization_);
            preparedStatement.setString(3, markNational_);
            preparedStatement.setString(4, markECTIS_);
            preparedStatement.setInt(5, markPoints_);
            preparedStatement.setString(6, credits_);
            preparedStatement.setString(7, workDescription_);
            preparedStatement.setString(8, namesCommission_);
            preparedStatement.setDate(9, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setDate(10, new java.sql.Date(zdt2.toInstant().toEpochMilli()));
            preparedStatement.setInt(11, workId);
            preparedStatement.execute();


            writeLog(String.format("UPDATE practics SET name_practice='%s',organization='%s',mark_national_scale='%s',mark_ectis='%s',mark_points=%d,credits='%s',work_description='%s', commission_names='%s',started='%s',ended='%s' WHERE id=%d", name_, organization_, markNational_, markECTIS_, markPoints_, credits_, workDescription_, namesCommission_, dateFrom_, dateTill_, workId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }

    }


    public static void addPractice(String name_, String organization_, String markNational_, String markECTIS_, int markPoints_, String credits_, LocalDate dateFrom_, LocalDate dateTill_, String workDescription_, String namesCommission_, int aspirantId) {
        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;

            LocalDateTime ldt = LocalDateTime.of(dateFrom_, LocalTime.of(16, 0));
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));


            LocalDateTime ldt2 = LocalDateTime.of(dateTill_, LocalTime.of(16, 0));
            ZonedDateTime zdt2 = ldt2.atZone(ZoneId.of("America/Los_Angeles"));


            String query = "INSERT INTO practics(name_practice,organization,mark_national_scale,mark_ectis,mark_points,credits,work_description,commission_names,started,ended,aspirant_id) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name_);
            preparedStatement.setString(2, organization_);
            preparedStatement.setString(3, markNational_);
            preparedStatement.setString(4, markECTIS_);
            preparedStatement.setInt(5, markPoints_);
            preparedStatement.setString(6, credits_);
            preparedStatement.setString(7, workDescription_);
            preparedStatement.setString(8, namesCommission_);
            preparedStatement.setDate(9, new java.sql.Date(zdt.toInstant().toEpochMilli()));
            preparedStatement.setDate(10, new java.sql.Date(zdt2.toInstant().toEpochMilli()));
            preparedStatement.setInt(11, aspirantId);
            preparedStatement.execute();


            writeLog("INSERT INTO practics(name_practice,organization,mark_national_scale,mark_ectis,mark_points,credits,work_description,commission_names,started,ended,aspirant_id) " +
                    "VALUES('" + name_ + "','" + organization_ + "','" + markNational_ + "','" + markECTIS_ + "'," + markPoints_ + ",'" + credits_ + "','" + workDescription_ + "','" + namesCommission_ + "','" + dateFrom_ + "','" + dateTill_ + "' , " + aspirantId + ")");


        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
        }

    }

    public static ObjectPractice getAspirantPracticeById(int id) {
        Connection connection = getConnection();
        ObjectPractice practice = null;

        try {

            PreparedStatement preparedStatement;
            String query = ("SELECT name_practice,organization,mark_national_scale,mark_ectis,mark_points,credits,work_description,commission_names,started,ended,id FROM practics WHERE id=?");
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet result1 = preparedStatement.executeQuery();


            java.util.Date dateFrom;
            java.util.Date dateTill;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");


            while (result1.next()) {

                dateFrom = targetFormat.parse(targetFormat.format(originalFormat.parse(result1.getDate(9).toString())));
                dateTill = targetFormat.parse(targetFormat.format(originalFormat.parse(result1.getDate(10).toString())));


                practice = new ObjectPractice(result1.getString(1), result1.getString(2), dateFrom, dateTill, result1.getString(3),
                        result1.getString(4), result1.getInt(5), result1.getString(6),
                        result1.getString(7), result1.getString(8), result1.getInt(11));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return practice;
    }

    public static void editDiploma(int id, String title, String description, int statusCode) {

        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;

            String query = "UPDATE diploma SET title= ?,description=?,result=? WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, statusCode);
            preparedStatement.setInt(4, id);
            preparedStatement.execute();

            writeLog("UPDATE diploma SET title='" + title + "',description='" + description + "',result=" + statusCode + " WHERE id=" + id);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
    }

    public static ObjectDiploma getDiplomaByAspirantId(int aspirantId) {


        Connection connection = getConnection();
        ObjectDiploma o = null;
        try {

            PreparedStatement preparedStatement;
            String query = "SELECT id,title,description,result FROM diploma WHERE aspirant_id=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, aspirantId);
            ResultSet result1 = preparedStatement.executeQuery();

            while (result1.next()) {
                o = new ObjectDiploma(result1.getInt("id"), result1.getString("title"), result1.getString("description"), result1.getInt("result"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return o;

    }

    public static void createDiploma(String title, String description, int statusCode, int aspirantId) {

        Connection connection = getConnection();
        try {

            PreparedStatement preparedStatement;

            String query = "INSERT INTO  diploma SET title= ?,description=?,result=?, aspirant_id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, statusCode);
            preparedStatement.setInt(4, aspirantId);
            preparedStatement.execute();

            writeLog("INSERT diploma SET title='" + title + "',description='" + description + "',result=" + statusCode + ", aspirant_id=" + aspirantId);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }

    }

    public static ObservableList<ObjectAspirant> getAllAspirantsWithDebts(int startYear, int endYear, int startCourse, int endCourse, int startSemestr, int endSemestr) {
        Connection connection = getConnection();
        ObservableList<ObjectAspirant> list = FXCollections.observableArrayList();
        try {

            Statement statement;
            statement = connection.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT  DISTINCT aspirant.id,aspirant.name, aspirant.birthday, vykladachi.name, specialities.name, aspirant.id_status,aspirant.notes, kafedra.room, aspirant.year, sf.form, aspirant.is_male " +
                    "FROM aspirant " +
                    "INNER JOIN  vykladachi ON aspirant.id_nav_kerivnik = vykladachi.id " +
                    "INNER JOIN specialities ON specialities.id = aspirant.id_speciality " +
                    "INNER JOIN kafedra ON kafedra.speciality_id = aspirant.id_speciality " +
                    "INNER JOIN study_form sf ON aspirant.study_form = sf.id " +
                    "INNER JOIN results r ON aspirant.id = r.aspirant_id " +
                    "WHERE aspirant.year>=" + startYear + " AND aspirant.year<=" + endYear + " AND r.course>=" + startCourse + " " +
                    "AND r.course<=" + endCourse + " AND r.semestr >=" + startSemestr + " AND r.semestr<=" + endSemestr + "" +
                    " AND (r.mark_national LIKE 'Не%' OR r.mark_ects LIKE 'F%' OR CAST(r.mark_points AS INT)<60) AND aspirant.status!=-1 " +
                    " ORDER BY aspirant.id ASC");


            java.util.Date date;
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat targetFormat = new SimpleDateFormat("dd.MM.yyyy");


            while (result1.next()) {

                date = originalFormat.parse(result1.getDate(3).toString());
                date = targetFormat.parse(targetFormat.format(date));
                ObjectAspirant o = new ObjectAspirant(result1.getInt(1), result1.getString(2), result1.getString(5),
                        date, result1.getString(4), result1.getInt(6), result1.getInt(9));
                o.setKafedraKabinet(result1.getString(8));
                o.setNote(result1.getString(7));

                o.setForm(result1.getString(10));
                o.setMale(result1.getInt(11) == 1 ? "Чоловіча" : "Жіноча");

                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

            if (connection != null)
                try {
                    connection.close();
                } catch (Exception e) {

                }
        }
        return list;


    }
}