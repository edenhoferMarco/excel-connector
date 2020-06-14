pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn verify'
            }
        }

        post {
            always {
                junit '**/surefire-reports/*.xml'
                junit '**/failsafe-reports/*.xml'
            }
        }
    }
}