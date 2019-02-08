def ciProject = 'labs-ci-cd'
def testProject = 'labs-test'
def devProject = 'labs-dev'
pipeline {
  agent {
    label 'jenkins-slave-mvn'
  }
  stages {
    stage('Build, Quality, And Security') {
      parallel {
        stage('Build App') {
          steps {
            container('jenkins-slave-mvn') {
              withEnv(["PATH=${overridePath}"]) {
                script {
                  def pom = readMavenPom file: 'pom.xml'
                  mvnVersion = pom.version
                  withSonarQubeEnv('sonar') {
                    try {
                      sh 'mvn install sonar:sonar'
                      timeout(time: 1, unit: 'HOURS') {
                        waitForQualityGate abortPipeline: true
                      }
                    } catch (error) {
                      publishHTML(target: [
                              reportDir            : 'target',
                              reportFiles          : 'dependency-check-report.html',
                              reportName           : 'OWASP Dependency Check Report',
                              keepAll              : true,
                              alwaysLinkToLastBuild: true,
                              allowMissing         : true
                      ])
                      publishHTML([
                              allowMissing         : true,
                              alwaysLinkToLastBuild: false,
                              keepAll              : true,
                              reportDir            : 'target/site/jacoco/',
                              reportFiles          : 'index.html',
                              reportName           : 'Jacoco Unit Test Report'
                      ])
                      zip dir: 'target/site/jacoco/',
                              glob: '',
                              zipFile: 'target/site/jacoco/jacoco-unit-tests.zip',
                              archive: false
    //                  emailext to: 'kfrankli@redhat.com',
    //                          attachmentsPattern: '**/*.zip',
    //                          subject: "Pipeline Build ${currentBuild.fullDisplayName} Unit Test Reports",
    //                          body: """Pipeline Build ${
    //                            currentBuild.fullDisplayName
    //                          } Unit Test Reports attached."""
                      throw error
                    }
                  }
                  def qualitygate = waitForQualityGate()
                  if (qualitygate.status != "OK") {
                    error "Pipeline aborted due to quality gate failure: ${qualitygate.status}"
                  }
                }
                publishHTML(target: [
                        reportDir            : 'target',
                        reportFiles          : 'dependency-check-report.html',
                        reportName           : 'OWASP Dependency Check Report',
                        keepAll              : true,
                        alwaysLinkToLastBuild: true,
                        allowMissing         : false
                ])
                publishHTML(target: [
                        reportDir            : 'target/site/jacoco/',
                        reportFiles          : 'index.html',
                        reportName           : 'Jacoco Unit Test Report',
                        allowMissing         : true,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true
                ])
                sh "mkdir jacoco-tmp && cp -r target/site/jacoco jacoco-tmp && rm jacoco-tmp/jacoco/jacoco-resources/*.js"
                zip dir: 'target/site/jacoco/',
                        glob: '',
                        zipFile: 'jacoco-unit-test-report.zip',
                        archive: true
                zip dir: 'jacoco-tmp/jacoco/',
                        glob: '',
                        zipFile: 'jacoco-unit-test-report-no-js.zip',
                        archive: false
              }
            }
          }
        }
      }
    }
    stage('Build Image') {
      steps {
        container('jenkins-slave-mvn') {
          script {
            withEnv(["PATH=${overridePath}"]) {
              sh 'oc login --token=$(cat /run/secrets/kubernetes.io/serviceaccount/token) --insecure-skip-tls-verify=true https://openshift.default.svc:443'
              sh "oc project ${ciProject}"
              sh "oc start-build ${env.PROJECT_NAME} --from-file=target/human-review-backend-${mvnVersion}.jar --wait"
            }
          }
        }
      }
    }
  }
}
