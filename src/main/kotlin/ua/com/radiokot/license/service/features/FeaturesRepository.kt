package ua.com.radiokot.license.service.features

interface FeaturesRepository {
    fun getFeatures(): Collection<Feature>
    operator fun get(featureIndex: Int): Feature? =
        getFeatures().find { it.index == featureIndex }
}
