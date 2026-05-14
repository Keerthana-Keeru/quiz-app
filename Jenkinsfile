pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK21'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master',
                url: 'https://github.com/Keerthana-Keeru/quiz-app.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Verify JAR') {
            steps {
                sh 'ls -l target/'
            }
        }
    }

    post {

        success {
            emailext (
                subject: "SUCCESS: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
Hello Keeru,

Your Quiz App CI/CD Pipeline executed successfully!

Project: ${JOB_NAME}
Build Number: ${BUILD_NUMBER}

Build URL:
${BUILD_URL}

Maven build, test, and package completed successfully.

Regards,
Jenkins
""",
                to: "keerthanakeeru200509@gmail.com"
            )
        }

        failure {
            emailext (
                subject: "FAILED: ${JOB_NAME} #${BUILD_NUMBER}",
                body: """
Hello Keeru,

Your Quiz App Pipeline failed.

Project: ${JOB_NAME}
Build Number: ${BUILD_NUMBER}

Check build logs here:
${BUILD_URL}

Regards,
Jenkins
""",
                to: "keerthanakeeru200509@gmail.com"
            )
        }
    }
}
