package test

import corda.flow.FruitFlows
import net.corda.client.rpc.CordaRPCClient
import net.corda.core.utilities.NetworkHostAndPort
import net.corda.core.utilities.getOrThrow

fun main(args: Array<String>) {
    val client = CordaRPCClient(NetworkHostAndPort("localhost", 10012)).start("user1", "test")

    val flowProgressHandle = client.proxy.startTrackedFlowDynamic(FruitFlows::class.java)
    flowProgressHandle.progress.subscribe {progressTrackerLabel ->
        System.out.println("Progress for  FruitFlows: "+progressTrackerLabel.toString())
    }
    val newFruitId = flowProgressHandle.returnValue.getOrThrow()
    println("Fruit created with $newFruitId")
}