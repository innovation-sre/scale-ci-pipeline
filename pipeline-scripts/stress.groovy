#!/usr/bin/env groovy

def pipeline_id = env.BUILD_ID
def node_label = NODE_LABEL.toString()
def stress = STRESS.toString().toUpperCase()
def property_file_name = "stress.properties"

println "Current pipeline job build id is '${pipeline_id}'"

// run stress  test
stage ('stress_scale_test') {
	if (stress == "TRUE") {
		currentBuild.result = "SUCCESS"
		node(node_label) {
			// get properties file
			if (fileExists(property_file_name)) {
				println "Looks like the property file already exists, erasing it"
				sh "rm ${property_file_name}"
			}
			// get properties file
			echo "Root Workspace: ${env.ROOT_WORKSPACE}"
			echo "Properties Prefix: ${env.PROPERTIES_PREFIX}"
			println "Current pipeline job build id is '${pipeline_id}'"
			sh "cat ${env.PROPERTIES_PREFIX}/${property_file_name}"
			def stress_properties = readProperties file: "${env.PROPERTIES_PREFIX}/${property_file_name}"
			def orchestration_host = stress_properties['ORCHESTRATION_HOST']
			def orchestration_user = stress_properties['ORCHESTRATION_USER']
			def workload_image = stress_properties['WORKLOAD_IMAGE']
			def workload_job_node_selector = stress_properties['WORKLOAD_JOB_NODE_SELECTOR']
			def workload_job_taint = stress_properties['WORKLOAD_JOB_TAINT']
			def workload_job_privileged = stress_properties['WORKLOAD_JOB_PRIVILEGED']
			def kubeconfig_file = stress_properties['KUBECONFIG_FILE']
			def pbench_instrumentation = stress_properties['PBENCH_INSTRUMENTATION']
			def enable_pbench_agents = stress_properties['ENABLE_PBENCH_AGENTS']
			def enable_pbench_copy = stress_properties['ENABLE_PBENCH_COPY']
			def pbench_server = stress_properties['PBENCH_SERVER']
			def scale_ci_results_token = stress_properties['SCALE_CI_RESULTS_TOKEN']
			def job_completion_poll_attempts = stress_properties['JOB_COMPLETION_POLL_ATTEMPTS']
			def snafu_user = stress_properties['SNAFU_USER']
			def snafu_cluster_name = stress_properties['SNAFU_CLUSTER_NAME']
			def es_host = stress_properties['ES_HOST']
			def es_port = stress_properties['ES_PORT']
			def es_index_prefix = stress_properties['ES_INDEX_PREFIX']
			def pbench_ssh_private_key_file = stress_properties['PBENCH_SSH_PRIVATE_KEY_FILE']
			def pbench_ssh_public_key_file = stress_properties['PBENCH_SSH_PUBLIC_KEY_FILE']
			def stress_prefix = stress_properties['STRESS_PREFIX']
			def stress_cleanup = stress_properties['STRESS_CLEANUP']
			def stress_basename = stress_properties['STRESS_BASENAME']
			def stress_max_nodes = stress_properties['STRESS_MAX_NODES']
			def stress_container_image = stress_properties['STRESS_CONTAINER_IMAGE']
			def stress_runtime = stress_properties['STRESS_RUNTIME']
			def stress_nodeselector = stress_properties['STRESS_NODESELECTOR']
			def stress_cpu = stress_properties['STRESS_CPU']
			def stress_cpu_load = stress_properties['STRESS_CPU_LOAD']
			def stress_io = stress_properties['STRESS_IO']
			def stress_mem = stress_properties['STRESS_MEM']
			def stress_daemons = stress_properties['STRESS_DAEMONS']
			def stress_additional_args = stress_properties['STRESS_ADDITIONAL_ARGS']
			def stress_mem_bytes = stress_properties['STRESS_MEM_BYTES']
			def stress_description = stress_properties['STRESS_DESCRIPTION']
			def stress_pause = stress_properties['STRESS_PAUSE']
			def stress_stepsize = stress_properties['STRESS_STEPSIZE']
			try {
				stress_build = build job: 'ATS-SCALE-CI-STRESS',
				parameters: [   [$class: 'StringParameterValue', name: 'SNAFU_USER', value: snafu_user],
				[$class: 'StringParameterValue', name: 'SNAFU_CLUSTER_NAME', value: snafu_cluster_name],
				[$class: 'StringParameterValue', name: 'ES_HOST', value: es_host],
				[$class: 'StringParameterValue', name: 'ES_PORT', value: es_port],
				[$class: 'StringParameterValue', name: 'ES_INDEX_PREFIX', value: es_index_prefix],
				[$class: 'StringParameterValue', name: 'ORCHESTRATION_HOST', value: orchestration_host],
				[$class: 'StringParameterValue', name: 'ORCHESTRATION_USER', value: orchestration_user],
				[$class: 'StringParameterValue', name: 'WORKLOAD_IMAGE', value: workload_image ],
				[$class: 'BooleanParameterValue', name: 'WORKLOAD_JOB_NODE_SELECTOR', value: Boolean.valueOf(workload_job_node_selector) ],
				[$class: 'BooleanParameterValue', name: 'WORKLOAD_JOB_TAINT', value: Boolean.valueOf(workload_job_taint)  ],
				[$class: 'BooleanParameterValue', name: 'WORKLOAD_JOB_PRIVILEGED', value: Boolean.valueOf(workload_job_privileged)  ],
				[$class: 'StringParameterValue', name: 'KUBECONFIG_FILE', value: kubeconfig_file ],
				[$class: 'BooleanParameterValue', name: 'PBENCH_INSTRUMENTATION', value: Boolean.valueOf(pbench_instrumentation)  ],
				[$class: 'BooleanParameterValue', name: 'ENABLE_PBENCH_AGENTS', value: Boolean.valueOf(enable_pbench_agents)  ],
				[$class: 'BooleanParameterValue', name: 'ENABLE_PBENCH_COPY', value: Boolean.valueOf(enable_pbench_copy)  ],
				[$class: 'StringParameterValue', name: 'PBENCH_SERVER', value: pbench_server ],
				[$class: 'StringParameterValue', name: 'SCALE_CI_RESULTS_TOKEN', value: scale_ci_results_token ],
				[$class: 'StringParameterValue', name: 'JOB_COMPLETION_POLL_ATTEMPTS', value: job_completion_poll_attempts ],
				[$class: 'StringParameterValue', name: 'PBENCH_SSH_PRIVATE_KEY_FILE', value: pbench_ssh_private_key_file ],
				[$class: 'StringParameterValue', name: 'PBENCH_SSH_PUBLIC_KEY_FILE', value: pbench_ssh_public_key_file ],
				[$class: 'StringParameterValue', name: 'STRESS_PREFIX', value: stress_prefix ],
				[$class: 'BooleanParameterValue', name: 'STRESS_CLEANUP', value: Boolean.valueOf(stress_cleanup)],
				[$class: 'StringParameterValue', name: 'STRESS_BASENAME', value: stress_basename],
				[$class: 'StringParameterValue', name: 'STRESS_MAX_NODES', value: stress_max_nodes],
				[$class: 'StringParameterValue', name: 'STRESS_CONTAINER_IMAGE', value: stress_container_image],
				[$class: 'StringParameterValue', name: 'STRESS_RUNTIME', value: stress_runtime],
				[$class: 'StringParameterValue', name: 'STRESS_CPU', value: stress_cpu],
				[$class: 'StringParameterValue', name: 'STRESS_CPU_LOAD', value: stress_cpu_load],
				[$class: 'StringParameterValue', name: 'STRESS_IO', value: stress_io],
				[$class: 'StringParameterValue', name: 'STRESS_MEM', value: stress_mem],
				[$class: 'StringParameterValue', name: 'STRESS_DAEMONS', value: stress_daemons],
				[$class: 'StringParameterValue', name: 'STRESS_ADDITIONAL_ARGS', value: stress_additional_args],
				[$class: 'StringParameterValue', name: 'STRESS_MEM_BYTES', value: stress_mem_bytes],
				[$class: 'StringParameterValue', name: 'STRESS_STEPSIZE', value: stress_stepsize],
				[$class: 'StringParameterValue', name: 'STRESS_PAUSE', value: stress_pause],
				[$class: 'StringParameterValue', name: 'STRESS_DESCRIPTION', value: stress_description],
				[$class: 'StringParameterValue', name: 'STRESS_NODESELECTOR', value: stress_nodeselector]]

			} catch ( Exception e) {
				echo "ATS-SCALE-CI-STRESS Job failed with the following error: "
				echo "${e.getMessage()}"
				currentBuild.result = "FAILURE"
 				sh "exit 1"
			}
			println "ATS-SCALE-CI-STRESS build ${stress_build.getNumber()} completed successfully"
		}
	}
}
