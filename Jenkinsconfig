pipeline {
    agent any

    tools {
        maven 'Maven3'   // Name of your Maven installation in Jenkins global tools
        jdk 'Java21'     // Name of JDK configured in Jenkins
    }

    environment {
        APP_NAME = "battleship"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
        post {
                 always {
                     echo "Pipeline finished: ${currentBuild.result}"
                 }
             }
         }