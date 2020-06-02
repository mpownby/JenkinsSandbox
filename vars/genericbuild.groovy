def call=(Map config=[:]) {
node {
    stage('SCM') {
		echo 'Gathering code from version control'
		git branch: '${branch}', url: 'https://github.com/mpownby/JenkinsSandbox'
    }
    stage('Build') {
        echo 'Building....'
		releasenotes(changes:"true")
    }
    stage('Test') {
        echo 'Testing....'
    }
    stage('Deploy') {
        echo 'Deploying....'
    }
}
}
