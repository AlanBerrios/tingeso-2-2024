pipeline{

    agent any
    tools{
        maven "maven"
    }

    stages{
        stage("Build JAR File"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/AlanBerrios/tingeso-2-2024']])
                dir("backendprestabanco"){
                    bat "mvn clean package"
                }

            }
        }
        stage("Test"){
            steps{
                dir("backendprestabanco"){
                    bat "mvn test"
                }
            }
        }

        stage("Build and Push Docker Image"){
            steps{
                dir("backendprestabanco"){
                    script{
                        bat "docker context use default"
                        withDockerRegistry(credentialsId: 'docker-credentials'){
                            bat "docker build -t alanberrios/prestabancobackend-image:latest ."
                            bat "docker push alanberrios/prestabancobackend-image:latest"
                        }
                    }
                }
            }
        }
    }
}