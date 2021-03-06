//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.03.12 at 02:29:08 PM IST 
//


package Infra;

import java.math.BigInteger;


public class Configuration
{
    private Debug Debug;

    public Debug getDebug() { return this.Debug; }

    public void setDebug(Debug Debug) { this.Debug = Debug; }

    private OpenCV OpenCV;

    public OpenCV getOpenCV() { return this.OpenCV; }

    public void setOpenCV(OpenCV OpenCV) { this.OpenCV = OpenCV; }

    private Lessons Lessons;

    public Lessons getLessons() { return this.Lessons; }

    public void setLessons(Lessons Lessons) { this.Lessons = Lessons; }

    private Database Database;

    public Database getDatabase() {
        return Database;
    }

    public void setDatabase(Database database) {
        Database = database;
    }

    public static class Lessons
    {
        private String PdfStore;

        public String getPdfStore() { return this.PdfStore; }

        public void setPdfStore(String PdfStore) { this.PdfStore = PdfStore; }
    }

    public static class Database
    {
        private String DBPath;

        public String getDBPath() {
            return DBPath;
        }

        public void setDBPath(String DBPath) {
            this.DBPath = DBPath;
        }
    }

    public static class OpenCV
    {
        private long DebugByID;

        public long getDebugByID() { return this.DebugByID; }

        public void setDebugByID(long DebugByID) { this.DebugByID = DebugByID; }

        private int SamplingIntervalMS;

        public int getSamplingIntervalMS() { return this.SamplingIntervalMS; }

        public void setSamplingIntervalMS(int SamplingInterval) { this.SamplingIntervalMS = SamplingInterval; }

        private int MinimumTimeForDistractionReportSec;

        public int getMinimumTimeForDistractionReportSec() {
            return MinimumTimeForDistractionReportSec;
        }

        public void setMinimumTimeForDistractionReportSec(int minimumTimeForDistractionReportSec) {
            MinimumTimeForDistractionReportSec = minimumTimeForDistractionReportSec;
        }

    }

    public static class Debug
    {
        private String logDir;

        public String getLogDir() { return this.logDir; }

        public void setLogDir(String logDir) { this.logDir = logDir; }

        private boolean DebugMode;

        public boolean getDebugMode() { return this.DebugMode; }

        public void setDebugMode(boolean DebugMode) { this.DebugMode = DebugMode; }
    }
}
