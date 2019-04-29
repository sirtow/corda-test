package corda.flow

import co.paralleluniverse.fibers.Suspendable
import corda.contract.FruitContract
import corda.contract.Lemon
import corda.contract.LemonContract
import net.corda.core.contracts.Command
import net.corda.core.contracts.TimeWindow
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.FinalityFlow
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import java.time.Instant

@InitiatingFlow
@StartableByRPC
@Suspendable
class FruitFlows: FlowLogic<UniqueIdentifier>() {

    companion object {
        object START : ProgressTracker.Step("FruitFlows: Initialising flow.")
        object SIGN : ProgressTracker.Step("FruitFlows: Signing our  Transaction.")
        object SESSION : ProgressTracker.Step("FruitFlows: Creating sessions.")
        object FINALITY : ProgressTracker.Step("FruitFlows: Starting FINALITY.")
        object DONE : ProgressTracker.Step("FruitFlows: Flow is done.")

        fun tracker() = ProgressTracker(START,SIGN,SESSION,FINALITY,DONE)
    }

    override val progressTracker = tracker()

    @Suspendable
    override fun call(): UniqueIdentifier {
        progressTracker.currentStep= START
        val notary = serviceHub.networkMapCache.notaryIdentities[0]
        val participants = serviceHub.networkMapCache.allNodes.map { it -> it.legalIdentities[0] } + ourIdentity
        val newFruit = Lemon(participants = participants, weight = "1kg")
        val command = Command(FruitContract.Commands.addFruit(), listOf(ourIdentity.owningKey))
        val txBuilder = TransactionBuilder(notary = notary)
                .addOutputState(newFruit, LemonContract::class.qualifiedName!!)
                .addCommand(command).setTimeWindow(TimeWindow.fromOnly(Instant.now()))
        progressTracker.currentStep= SIGN
        val ptx = serviceHub.signInitialTransaction(txBuilder)

        progressTracker.currentStep= SESSION
        val sessionList = newFruit.participants.filter{it->it.owningKey!=ourIdentity.owningKey}.map {
            initiateFlow(it)
        }
        progressTracker.currentStep= FINALITY
        val subFlow = subFlow(FinalityFlow(ptx, sessionList))
        progressTracker.currentStep= DONE
        return newFruit.linearId
    }

}