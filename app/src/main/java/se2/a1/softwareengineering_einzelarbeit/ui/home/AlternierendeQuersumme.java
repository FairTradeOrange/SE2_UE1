package se2.a1.softwareengineering_einzelarbeit.ui.home;

/**
 * Hilfsklasse um die alternierende Quersumme zu berechnen.
 *
 * Die alternierende Quersumme (auch Querdifferenz, Paarquersumme oder Wechselsumme genannt)[2] erhält man,
 * indem man die Ziffern einer Zahl abwechselnd subtrahiert und addiert. Dabei kann links oder rechts begonnen werden.
 * Im Folgenden wird von rechts begonnen. So ist für die Zahl n = 36036 die alternierende Quersumme aqs(n) = 6 − 3 + 0 − 6 + 3 = 0.
 *
 * Gleichwertig dazu ist das folgende Verfahren (die Zählung der Ziffern soll wieder rechts beginnen):
 *   -  Man addiert zum Wert der ersten Ziffer den der dritten, fünften, siebten usw.
 *   -  Man addiert zum zweiten Ziffernwert den vierten, sechsten, achten usw.
 *   -  Subtrahiert man nun von der ersten Summe die zweite, so erhält man die alternierende Quersumme.
 *
 *   --> siehe <a href="https://de.wikipedia.org/wiki/Quersumme">Wikipedia</a>
 */
public class AlternierendeQuersumme {

    private int matrikelNummer;
    private int querSumme;
    int[] ziffern;

    public AlternierendeQuersumme(String Matrikelnummer){
        this.ziffern = new int[Matrikelnummer.length()];
        this.matrikelNummer = Integer.parseInt(Matrikelnummer);
        for (int i = 0; i < Matrikelnummer.length(); i++) {
            ziffern[i] = Character.getNumericValue(Matrikelnummer.charAt(i));
        }
    }

    public int solve(){
        int summe = 0;
        for (int i = 0; i < ziffern.length; i++) {
            //Index gerade --> Ziffer addieren, sonst subtraieren
            if (i % 2 == 0) {
                summe += ziffern[i];
            } else {
                summe -= ziffern[i];
            }
        }
        return summe;
    }

}
