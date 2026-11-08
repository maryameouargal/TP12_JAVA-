package com.example.filemanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.StandardCopyOption;
import java.nio.file.NoSuchFileException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

public class Main {

	public static void main(String[] args) {
		System.out.println("Démarrage du TP 12 : Gestion des fichiers ");

		// PARTIE DES TESTS

		System.out.println("\n[Test 1]:: Lecture Classique  avec BufferedReader :");
		readFileWithBufferedReader("data/employees.txt");

		System.out.println("\n[Test 2]:: Écriture  Classique  avec BufferedWriter :");
		writeFileWithBufferedWriter("data/output.txt", "Ceci est mon fichier de sortie.");

		System.out.println("\n[Test 3]:: Écriture formatée avec PrintWriter :");
		writeWithPrintWriter("data/report.txt");

		System.out.println("\n[Test 4]:: Lecture avec l'API Files :");
		readWithFilesApi("data/employees.txt");

		System.out.println("\n[Test 5]:: Écriture avec l'API Files :");
		writeWithFilesApi("data/nio_output.txt", "Fichier généré via NIO.");

		System.out.println("\n[Test 6]:: Copie et Déplacement :");
		copyFile("data/employees.txt", "data/employees_backup.txt");
		moveFile("data/nio_output.txt", "data/moved_output.txt");

		System.out.println("\n[Test 7]::  Suppression :");
		deleteFile("data/moved_output.txt");
		// Test de l' erreur
		deleteFile("data/fichier_inexistant.txt");

		System.out.println("\n[Test 8]:: Métadonnées :");
		getFileMetadata("data/employees.txt");

		System.out.println("\n[Test 9] :: Binaire :");
		byte[] testData = "Données binaires de test".getBytes();
		writeBinaryFile("data/copy_binary.bin", testData);
		readBinaryFile("data/copy_binary.bin");
	}

	// ÉTAPE 3 : Lecture avec FileReader et BufferedReader
	public static void readFileWithBufferedReader(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println("Ligne qui a été lue : " + line);
			}
		} catch (IOException e) {
			System.out.println(" il y'a une erreur d'E/S  lors de la lecture : " + e.getMessage());
		}
	}

	// ÉTAPE 4 : Écriture avec FileWriter et BufferedWriter
	public static void writeFileWithBufferedWriter(String filePath, String content) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			bw.write(content);
			bw.newLine();
			bw.write("Ligne ajoutée avec succès!!!");
			System.out.println("Écriture réussie dans : " + filePath);
		} catch (IOException e) {
			System.out.println("il y 'a une erreur d'E/S lors de l'écriture : " + e.getMessage());
		}
	}

	// ÉTAPE 5 : Utilisation de PrintWriter
	public static void writeWithPrintWriter(String filePath) {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(filePath))) {
			pw.println(" Rapport des employés");
			pw.printf("Nom: %s, Âge: %d, Poste: %s%n", "Rina", 27, "Engineer");
			pw.printf("Nom: %s, Âge: %d, Poste: %s%n", "Marcus", 24, "Manager");
			pw.println("Fin du rapport.");
			System.out.println("Rapport généré avec succès.");
		} catch (IOException e) {
			System.out.println("ily'a une erreur d'E/S  lors de l'écriture du rapport : " + e.getMessage());
		}
	}

	// ÉTAPE 6 : Lecture avec l'API java.nio.file.Files
	public static void readWithFilesApi(String filePath) {
		try {
			Path path = Paths.get(filePath);
			List<String> lines = Files.readAllLines(path);
			for (String line : lines) {
				System.out.println("NIO Lecture  réussie : " + line);
			}
		} catch (IOException e) {
			System.out.println("Erreur d'E/S (NIO) qu'on vecu lors de la lecture : " + e.getMessage());
		}
	}

	// ÉTAPE 6 : Écriture avec l'API java.nio.file.Files
	public static void writeWithFilesApi(String filePath, String content) {
		try {
			Path path = Paths.get(filePath);
			// On ajoute un petit getBytes() pour respecter l'encodage
			Files.write(path, content.getBytes());
			System.out.println("Fichier NIO écrit avec succès : " + filePath);
		} catch (IOException e) {
			System.out.println(" il y'a une erreur d'E/S (NIO)  lors de l'écriture : " + e.getMessage());
		}
	}

	// ÉTAPE 7 : Copie de fichier
	public static void copyFile(String source, String destination) {
		try {
			Path sourcePath = Paths.get(source);
			Path destPath = Paths.get(destination);
			Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Copie réussie : " + source + " vers " + destination);
		} catch (IOException e) {
			System.out.println("il y'a une erreur d'E/S lors de la copie : " + e.getMessage());
		}
	}

	// ÉTAPE 7 : Déplacement de fichier
	public static void moveFile(String source, String destination) {
		try {
			Path sourcePath = Paths.get(source);
			Path destPath = Paths.get(destination);
			Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Déplacement a été réussi : " + source + " vers " + destination);
		} catch (IOException e) {
			System.out.println("il y'a une erreur d'E/S lors du déplacement : " + e.getMessage());
		}
	}

	// ÉTAPE 8 : Suppression de fichier et gestion des erreurs
	public static void deleteFile(String filePath) {
		try {
			Path path = Paths.get(filePath);
			if (Files.exists(path)) {
				Files.delete(path);
				System.out.println("le fichier a été  supprimé : " + filePath);
			} else {
				System.out.println("Le fichier est introuvable : " + filePath);
			}
		} catch (NoSuchFileException e) {
			System.out.println("Suppression annulée, fichier déjà supprimé : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("il y'a une erreur d'E/S lors de la suppression : " + e.getMessage());
		}
	}

	// ÉTAPE 9 : Lecture de fichier binaire
	public static void readBinaryFile(String filePath) {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			long totalBytes = 0;
			while ((bytesRead = bis.read(buffer)) != -1) {
				totalBytes += bytesRead;
			}
			System.out.println("Lecture binaire terminée pour : " + filePath);
			System.out.println("Taille totale en octets est : " + totalBytes + " octets");
		} catch (IOException e) {
			System.out.println("il y'a une erreur d'E/S lors de la lecture binaire : " + e.getMessage());
		}
	}

	// ÉTAPE 10 : Écriture de fichier binaire avec FileOutputStream
	public static void writeBinaryFile(String filePath, byte[] data) {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
			bos.write(data);
			bos.flush();
			System.out.println("Écriture binaire a réussie : " + filePath);
			System.out.println("Taille écrite : " + data.length + " octets");
		} catch (IOException e) {
			System.out.println("il y'a une erreur d'E/S lors de l'écriture binaire : " + e.getMessage());
		}
	}

	// ÉTAPE 11 :Gestion des métadonnées de fichiers
	public static void getFileMetadata(String filePath) {
		try {
			Path path = Paths.get(filePath);
			BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
			System.out.println("Métadonnées : " + filePath);
			System.out.println(" la taille est  : " + attrs.size() + " octets");
			System.out.println("la dernière modification  est : " + attrs.lastModifiedTime().toInstant());
			System.out.println("Est un répertoire : " + attrs.isDirectory());
			System.out.println("Est un fichier : " + attrs.isRegularFile());
		} catch (IOException e) {
			System.out.println("il y'a une erreur d'E/S lors de la lecture des métadonnées : " + e.getMessage());
		}
	}
}