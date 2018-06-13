package com.example.dan.opsctask2;

import java.util.Date;

/**
 * Created by Dan on 6/13/2018.
 */

public class LogSteps {

        private Date date;
        private int steps;

        public LogSteps(Date date, int steps) {
            this.date = date;
            this.steps = steps;
        }

        public Date getDate() {
            return date;
        }

        public int getSteps() {
            return steps;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }
    }


