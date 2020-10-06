#!/usr/bin/env groovy

def contact = "nelluri@redhat.com"
def tooling = TOOLING.toString().toUpperCase()
def fio = FIO.toString().toUpperCase()
def stress = STRESS.toString().toUpperCase()
def run_conformance = CONFORMANCE.toString().toUpperCase()
def nodevertical = NODEVERTICAL_SCALE_TEST.toString().toUpperCase()
def mastervertical = MASTERVERTICAL_SCALE_TEST.toString().toUpperCase()
def http = HTTP_TEST.toString().toUpperCase()
def services_per_namespace = SERVICES_PER_NAMESPACE.toString().toUpperCase()
def podvertical = PODVERTICAL.toString().toUpperCase()
def deployments_per_ns = DEPLOYMENTS_PER_NS.toString().toUpperCase()
def ns_per_cluster = NS_PER_CLUSTER.toString().toUpperCase()
def networking = NETWORKING.toString().toUpperCase()
def kraken = KRAKEN.toString().toUpperCase()
def node_label = NODE_LABEL.toString()
def run_uperf = UPERF.toString().toUpperCase()
def token = TOKEN.toString()
def url = API_URL.toString()
node (node_label) {
	// setup the repo containing the pipeline scripts
	stage('login to cluster') {
		if (token?.trim() && url?.trim()) {
			sh "env"
			try {
				sh """
				export OPTIONS="-o StrictHostKeyChecking=no -o ServerAliveInterval=30 -o ServerAliveCountMax=1200"
				chmod 600 ${PRIVATE_KEY}
		
				# fetch the kubeconfig from the orchestration host
				echo "Fetching the kubeconfig from the orchestration host"
				
				ssh ${OPTIONS} -i ${PRIVATE_KEY} ${ORCHESTRATION_USER}@${ORCHESTRATION_HOST} oc login ${URL} --token=${TOKEN}
				"""
			}
			catch (exc) {
				echo 'Error occurred during login command.'
				throw exc
			}
		}
	}

	stage('cloning pipeline repo') {
		checkout scm
		env.ROOT_WORKSPACE = "${env.WORKSPACE}"
		env.PROPERTIES_PREFIX = "${env.WORKSPACE}/properties-files/"
		echo "Root Workspace: ${env.ROOT_WORKSPACE}"
		echo "Properties Prefix: ${env.PROPERTIES_PREFIX}"
	}
	// stage to setup pbench
	if (tooling == "TRUE") {
		load "pipeline-scripts/tooling.groovy"
	}

	if (fio == "TRUE") {
		load "pipeline-scripts/fio.groovy"
	}

	if (stress == "TRUE") {
		load "pipeline-scripts/stress.groovy"
	}

	// stage to run conformance
	if (run_conformance == "TRUE") {
		load "pipeline-scripts/conformance.groovy"
	}

	// stage to run nodevertical scale test
	if (nodevertical == "TRUE") {
		load "pipeline-scripts/nodevertical.groovy"
	}

	// stage to run http scale test
	if (http == "TRUE") {
		load "pipeline-scripts/http.groovy"
	}

	// stage to run services per namespace test
	if (services_per_namespace == "TRUE") {
		load "pipeline-scripts/services_per_namespace.groovy"
	}

	// stage to run deployments per ns test
	if ( deployments_per_ns == "TRUE") {
		load "pipeline-scripts/deployments_per_ns.groovy"
	}

	// stage to run podvertical test
	if ( podvertical == "TRUE") {
		load "pipeline-scripts/podvertical.groovy"
	}

	// stage to run networking test
	if ( networking == "TRUE") {
		load "pipeline-scripts/networking.groovy"
	}

	// stage to run mastervertical scale test
	if (mastervertical == "TRUE") {
		load "pipeline-scripts/mastervertical.groovy"
	}

	// stage to run ns_per_cluster test
	if ( ns_per_cluster == "TRUE") {
		load "pipeline-scripts/ns_per_cluster.groovy"
	}

	// stage to run kraken test
	if (kraken == "TRUE") {
		load "pipeline-scripts/kraken.groovy"
	}

	if (run_uperf == "TRUE") {
		load "pipeline-scripts/uperf.groovy"
	}


	// cleanup the workspace
	stage('cleaning workspace') {
		deleteDir()
	}
}
