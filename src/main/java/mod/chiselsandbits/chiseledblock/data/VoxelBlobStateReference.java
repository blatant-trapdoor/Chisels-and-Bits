package mod.chiselsandbits.chiseledblock.data;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.util.AxisAlignedBB;

public class VoxelBlobStateReference implements Comparable<VoxelBlobStateReference>
{

	private static Map<VoxelBlobStateInstance, WeakReference<VoxelBlobStateInstance>> innerRefs = Collections.synchronizedMap( new WeakHashMap<VoxelBlobStateInstance, WeakReference<VoxelBlobStateInstance>>() );

	private static VoxelBlobStateInstance lookupRef(
			final VoxelBlobStateInstance inst )
	{
		final WeakReference<VoxelBlobStateInstance> o = innerRefs.get( inst );

		if ( o != null )
		{
			return o.get();
		}

		return null;
	}

	private static void addRef(
			final VoxelBlobStateInstance inst )
	{
		innerRefs.put( inst, new WeakReference<VoxelBlobStateInstance>( inst ) );
	}

	private static VoxelBlobStateInstance FindRef(
			final byte[] v )
	{
		final VoxelBlobStateInstance t = new VoxelBlobStateInstance( v );
		VoxelBlobStateInstance ref = null;

		ref = lookupRef( t );

		if ( ref == null )
		{
			ref = t;
			addRef( t );
		}

		return ref;
	}

	private final VoxelBlobStateInstance data;
	public final long weight;

	public byte[] getByteArray()
	{
		return data.v;
	}

	public VoxelBlob getVoxelBlob()
	{
		return data.getBlob();
	}

	public VoxelBlobStateReference(
			final VoxelBlob blob,
			final long weight )
	{
		this( blob.toByteArray(), weight );
	}

	public VoxelBlobStateReference(
			final byte[] v,
			final long weight )
	{
		data = FindRef( v );
		this.weight = weight;
	}

	@Override
	public boolean equals(
			final Object obj )
	{
		if ( !( obj instanceof VoxelBlobStateReference ) )
		{
			return false;
		}

		final VoxelBlobStateReference second = (VoxelBlobStateReference) obj;
		return data.equals( second.data ) && second.weight == weight;
	}

	@Override
	public int hashCode()
	{
		return data.hash ^ (int) ( weight ^ weight >>> 32 );
	}

	@Override
	public int compareTo(
			final VoxelBlobStateReference o )
	{
		final int comp = data.compareTo( o.data );
		if ( comp == 0 )
		{
			if ( weight == o.weight )
			{
				return 0;
			}

			return weight < o.weight ? -1 : 1;
		}
		return comp;
	}

	public List<AxisAlignedBB> getOcclusionBoxes()
	{
		return data.getOcclusionBoxes();
	}

}
