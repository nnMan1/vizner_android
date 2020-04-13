package com.example.myapplication;

import android.util.Log;

public class Vizener {
    String encrypt(String text, String key) {
        String encrypted = "";

        Log.d("Key = ", key);

        for(int i = 0;i<text.length();i++) {
            char c = text.charAt(i);
            if(c>= 'a' && c <= 'z') {
                c = (char) ((c + key.charAt(i % key.length()) - 2 * 'a') % 26 + 'a');
                encrypted += c;
                continue;
            }

            if(c>= 'A' && c <= 'Z') {
                c = (char) ((c + key.charAt(i % key.length()) - 'a' - 'A') % 26 + 'A');
                encrypted += c;
                continue;
            }

            encrypted += c;
        }

        return encrypted;
    }

    private Pair<int[], Integer> getCharactersFrequencies(String text, int keyLenhth, int offset) {
        int[] chars = new int[26];
        text = text.toLowerCase();
        int br = 0;

        for(int i=offset;i<text.length(); i+=keyLenhth) {
            char c = text.charAt(i);
            if (c >= 'a' && c<='z') {
                chars[c-'a']++;
                br++;
            }
        }

        if (br == 0) { return new Pair<int[],Integer>(new int[0], 0); }

        return new Pair(chars, br);
    }

    private double arrayDistance(double[] a, double[] b) {
        double distance = 0;

        if (a.length != b.length) return 1<<30;

        for(int i=0;i<a.length;i++) {
            distance += Math.abs(a[i] - b[i]);
        }

        return distance;
    }

    private int getKeyLength(String text) {

        int bl = -1;
        double blv = 1<<30;

        for(int i=1;i<text.length();i++) {
            Pair<int[], Integer> frequencies = getCharactersFrequencies(text, i,0);

            double indCo = 0;

            for(int j=0;j<frequencies.a.length;j++) {
                indCo += frequencies.a[j] * (frequencies.a[j] - 1);
            }

            if(frequencies.b < 2) { // ima previse bjelina
                return -1;
            }

            indCo/=frequencies.b * (frequencies.b - 1);

            if(blv > Math.abs(indCo - 0.065)) {
                blv = Math.abs(indCo - 0.065);
                bl = i;
            }

            if(blv<0.008) break;
        }

        return bl;
    }

    private String decrypt(String text, String key) {
        String decrypted = "";

        Log.d("Key = ", key);

        for(int i = 0;i<text.length();i++) {
            char c = text.charAt(i);
            if(c>= 'a' && c <= 'z') {
                c = (char) ((c - key.charAt(i % key.length()) + 26) % 26 + 'a');
                decrypted += c;
                continue;
            }

            if(c>= 'A' && c <= 'Z') {
                c = (char) ((c - key.charAt(i % key.length()) + 'a' - 'A' + 26) % 26 + 'A');
                decrypted += c;
                continue;
            }

            decrypted += c;
        }

        return decrypted;
    }

    Pair<String, String> decrypt(String text) {
        int keyLength = getKeyLength(text);

        String key = "";

        text = text.toLowerCase();

        double[] eacf = {   0.08167, 0.01492, 0.02202,	0.04253, 0.12702,
                0.02228, 0.02015, 0.06094, 0.06966, 0.00153,
                0.01292, 0.04025, 0.02406, 0.06749, 0.07507,
                0.01929, 0.00095, 0.05987, 0.06327, 0.09356,
                0.02758, 0.00978, 0.02560, 0.00150, 0.01994,
                0.00077 };

        for(int i=0;i<keyLength;i++) {

            Pair<int[], Integer> frequencies = getCharactersFrequencies(text, keyLength, i);
            double[] cf = new double[26];
            for(int j=0;j<26;j++) cf[j]=(double)(frequencies.a[j])/frequencies.b;

            double dist = arrayDistance(cf, eacf);
            int offset = 0;

            for(int j = 0;j<24;j++) {
                double tmp = cf[0];
                for(int z=0;z<25;z++) {
                    cf[z] = cf[z+1];
                }
                cf[25] = tmp;

                if(arrayDistance(cf, eacf) < dist) {
                    offset = j + 1;
                    dist = arrayDistance(cf, eacf);
                }
            }

            key += (char)('a' + offset);
        }

        Log.d("key length = ", String.valueOf(keyLength));
        return new Pair(decrypt(text, key), key);
    }
}
