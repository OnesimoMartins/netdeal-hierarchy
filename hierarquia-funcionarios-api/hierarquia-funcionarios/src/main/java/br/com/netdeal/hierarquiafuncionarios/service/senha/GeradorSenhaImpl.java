package br.com.netdeal.hierarquiafuncionarios.service.senha;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class GeradorSenhaImpl implements GeradorSenha{

    @Value("${app.security.algorithm}")
    private String ALGORITHM = "AES";
    @Value("${app.security.transformation}")
    private String TRANSFORMATION;
    @Value("${app.security.secret-key}")
    private String SECRET_KEY;

    @Override
    @SneakyThrows
    public String gerarEncriptacao(String senha)  {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] iv = cipher.getIV();
        byte[] encryptedBytes = cipher.doFinal(senha.getBytes());

        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    @SneakyThrows
    public String reverterEncriptacao(String senha) {
        byte[] combined = Base64.getDecoder().decode(senha);

        if (combined.length < 16) {
            throw new IllegalArgumentException("String criptografada é muito curta.");
        }

        byte[] iv = new byte[16];
        System.arraycopy(combined, 0, iv, 0, iv.length);

        byte[] encryptedBytes = new byte[combined.length - iv.length];
        System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    @Override
    public int getSenhaScore(String senha) {

        var minLength = 8;
        int nAlphaUC = 0, nAlphaLC = 0, nNumber = 0, nSymbol = 0;
        int nMidChar = 0, nUnqChar = 0, nRepChar = 0;
        Double nRepInc = (double) 0, nScore = (double) 0;
        int nConsecAlphaUC = 0, nConsecAlphaLC = 0, nConsecNumber = 0;
        int nSeqAlpha = 0, nSeqNumber = 0, nSeqSymbol = 0, nReqChar = 0;

        final int nMultMidChar = 2, nMultConsecAlphaUC = 2, nMultConsecAlphaLC = 2;
        final int nMultConsecNumber = 2, nMultSeqAlpha = 3, nMultSeqNumber = 3, nMultSeqSymbol = 3;
        final int nMultLength = 4, nMultNumber = 4, nMultSymbol = 6;

        int nLength = senha.length();
        nScore = (double) (nLength * nMultLength);
        String[] arrPwd = senha.replaceAll("\\s+", "").split("");
        int arrPwdLen = arrPwd.length;

        // Variáveis temporárias
        int nTmpAlphaUC = -1, nTmpAlphaLC = -1, nTmpNumber = -1, nTmpSymbol = -1;
        String sAlphas = "abcdefghijklmnopqrstuvwxyz";
        String sNumerics = "01234567890";
        String sSymbols = ")!@#$%^&*()";

        // Loop principal
        for (int a = 0; a < arrPwdLen; a++) {
            String currentChar = arrPwd[a];

            if (currentChar.matches("[A-Z]")) {
                if (nTmpAlphaUC != -1 && (nTmpAlphaUC + 1) == a) {
                    nConsecAlphaUC++;
                }
                nTmpAlphaUC = a;
                nAlphaUC++;
            } else if (currentChar.matches("[a-z]")) {
                if (nTmpAlphaLC != -1 && (nTmpAlphaLC + 1) == a) {
                    nConsecAlphaLC++;
                }
                nTmpAlphaLC = a;
                nAlphaLC++;
            } else if (currentChar.matches("[0-9]")) {
                if (a > 0 && a < (arrPwdLen - 1)) {
                    nMidChar++;
                }
                if (nTmpNumber != -1 && (nTmpNumber + 1) == a) {
                    nConsecNumber++;
                }
                nTmpNumber = a;
                nNumber++;
            } else if (currentChar.matches("[^a-zA-Z0-9_]")) {
                if (a > 0 && a < (arrPwdLen - 1)) {
                    nMidChar++;
                }
                if (nTmpSymbol != -1 && (nTmpSymbol + 1) == a) {
                }
                nTmpSymbol = a;
                nSymbol++;
            }

            // Checagem de caracteres repetidos
            boolean bCharExists = false;
            for (int b = 0; b < arrPwdLen; b++) {
                if (a != b && currentChar.equals(arrPwd[b])) {
                    bCharExists = true;
                    nRepInc += Math.abs(arrPwdLen / (b - a));
                }
            }

            if (bCharExists) {
                nRepChar++;
                nUnqChar = arrPwdLen - nRepChar;
                nRepInc = (nUnqChar > 0) ? Math.ceil(nRepInc / nUnqChar) : Math.ceil(nRepInc);
            }
        }

        // Checar sequências alfabéticas
        for (int s = 0; s < 23; s++) {
            String sFwd = sAlphas.substring(s, Math.min(s + 3, sAlphas.length()));
            String sRev = new StringBuilder(sFwd).reverse().toString();
            if (senha.toLowerCase().contains(sFwd) || senha.toLowerCase().contains(sRev)) {
                nSeqAlpha++;
            }
        }

        // Checar sequências numéricas
        for (int s = 0; s < 8; s++) {
            String sFwd = sNumerics.substring(s, Math.min(s + 3, sNumerics.length()));
            String sRev = new StringBuilder(sFwd).reverse().toString();
            if (senha.toLowerCase().contains(sFwd) || senha.toLowerCase().contains(sRev)) {
                nSeqNumber++;
            }
        }

        // Checar sequências de símbolos
        for (int s = 0; s < 8; s++) {
            String sFwd = sSymbols.substring(s, Math.min(s + 3, sSymbols.length()));
            String sRev = new StringBuilder(sFwd).reverse().toString();
            if (senha.toLowerCase().contains(sFwd) || senha.toLowerCase().contains(sRev)) {
                nSeqSymbol++;
            }
        }

        // Pontuação baseada em uso
        if (nAlphaUC > 0 && nAlphaUC < nLength) {
            nScore += ((nLength - nAlphaUC) * 2);
        }
        if (nAlphaLC > 0 && nAlphaLC < nLength) {
            nScore += ((nLength - nAlphaLC) * 2);
        }
        if (nNumber > 0 && nNumber < nLength) {
            nScore += (nNumber * nMultNumber);
        }
        if (nSymbol > 0) {
            nScore += (nSymbol * nMultSymbol);
        }
        if (nMidChar > 0) {
            nScore += (nMidChar * nMultMidChar);
        }

        // Deduções por práticas ruins
        if ((nAlphaLC > 0 || nAlphaUC > 0) && nSymbol == 0 && nNumber == 0) { // Apenas letras
            nScore -= nLength;
        }
        if (nAlphaLC == 0 && nAlphaUC == 0 && nSymbol == 0 && nNumber > 0) { // Apenas números
            nScore -= nLength;
        }
        if (nRepChar > 0) { // O mesmo caractere existe mais de uma vez
            nScore -= nRepInc;
        }
        if (nConsecAlphaUC > 0) { // Letras maiúsculas consecutivas
            nScore -= (nConsecAlphaUC * nMultConsecAlphaUC);
        }
        if (nConsecAlphaLC > 0) { // Letras minúsculas consecutivas
            nScore -= (nConsecAlphaLC * nMultConsecAlphaLC);
        }
        if (nConsecNumber > 0) { // Números consecutivos
            nScore -= (nConsecNumber * nMultConsecNumber);
        }
        if (nSeqAlpha > 0) { // Sequências alfabéticas
            nScore -= (nSeqAlpha * nMultSeqAlpha);
        }
        if (nSeqNumber > 0) { // Sequências numéricas
            nScore -= (nSeqNumber * nMultSeqNumber);
        }
        if (nSeqSymbol > 0) { // Sequências de símbolos
            nScore -= (nSeqSymbol * nMultSeqSymbol);
        }

        // Checar requisitos mínimos
        int[] arrChars = {nLength, nAlphaUC, nAlphaLC, nNumber, nSymbol};
        String[] arrCharsIds = {"nLength", "nAlphaUC", "nAlphaLC", "nNumber", "nSymbol"};

        for (int c = 0; c < arrChars.length; c++) {
            int minVal = (arrCharsIds[c].equals("nLength")) ? minLength - 1 : 0;
            if (arrChars[c] >= (minVal + 1)) {
                nReqChar++;
            }
        }

        int nMinReqChars = (senha.length() >= minLength) ? 3 : 4;
        if (nReqChar > nMinReqChars) { // Um ou mais caracteres obrigatórios existem
            nScore += (nReqChar * 2);
        }

        // Limitar o score entre 0 e 100
        nScore = Math.min(Math.max( nScore, 0), 100);

        return nScore.intValue();
    }

}
