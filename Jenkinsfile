import org.jenkinsci.plugins.docker.workflow.*

pipeline {
    agent {
        docker {
            image("maven:3.9.6-openjdk-17-alpine")
        }
    }
    stages {
        stage("build") {
            steps {
                sh("mvn --version")
                sh("mvn clean install")
                // Добавьте другие шаги сборки вашего проекта, если необходимо
            }
        }
        // Добавьте другие этапы, если необходимо
    }
}
