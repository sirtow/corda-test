package corda.contract

import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.serialization.CordaSerializable

@CordaSerializable
@BelongsToContract(LemonContract::class)
class Lemon(
        val weight:String,

        override val linearId: UniqueIdentifier = UniqueIdentifier(), override val participants: List<Party>) : Fruit, LinearState {
    override fun getColor(): List<String> {
        return listOf("green","yellow")
    }

}
