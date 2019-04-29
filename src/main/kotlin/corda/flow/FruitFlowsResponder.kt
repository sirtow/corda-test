package corda.flow

import co.paralleluniverse.fibers.Suspendable
import corda.contract.FruitContract
import corda.contract.Lemon
import net.corda.core.contracts.TimeWindow
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.flows.*
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import java.time.Instant


@InitiatedBy(value =FruitFlows::class)
class FruitFlowsResponder(private val otherPartySession: FlowSession) :FlowLogic<SignedTransaction>() {

    companion object {
        object START : ProgressTracker.Step("FruitFlowsResponder: Initialising flow.")
        object SIGNED : ProgressTracker.Step("FruitFlowsResponder: responder signed.")
        object FINALITY : ProgressTracker.Step("FruitFlowsResponder: Finished  ReceiveFinalityFlow.")
        object DONE : ProgressTracker.Step("FruitFlowsResponder: Flow is done.")

        fun tracker() = ProgressTracker(START,SIGNED,FINALITY,DONE)
    }

    override val progressTracker = tracker()

    @Suspendable
    override fun call(): SignedTransaction {
        progressTracker.currentStep= START
        val signTransactionFlow = object : SignTransactionFlow(otherPartySession) {
            override fun checkTransaction(stx: SignedTransaction) {
            }
        }

        val txId = subFlow(signTransactionFlow).id
        progressTracker.currentStep= SIGNED
        val subFlow = subFlow(ReceiveFinalityFlow(otherPartySession, expectedTxId = txId))
        progressTracker.currentStep= DONE
        return subFlow
    }

}