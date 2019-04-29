package corda.contract

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.transactions.LedgerTransaction

class AFruitContract: Contract {
    interface Commands : CommandData {
        class addFruit : TypeOnlyCommandData(), Commands

    }

    override fun verify(tx: LedgerTransaction) {
        println("============ AFruitContract :: verify  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        println("Fruit color is : ${tx.outputsOfType<Fruit>()[0].getColor()}")
//
    }
}