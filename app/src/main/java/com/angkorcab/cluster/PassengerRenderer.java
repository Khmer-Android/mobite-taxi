package com.angkorcab.cluster;

/**
 * Created by phuong on 21-Nov-16.
 */
/*

public class PassengerRenderer extends DefaultClusterRenderer<Person> {
    private final IconGenerator mIconGenerator = new IconGenerator(context);
    private final IconGenerator mClusterIconGenerator = new IconGenerator(context);
    private final ImageView mImageView;
    private final ImageView mClusterImageView;
    private final int mDimension;
    private Context context;
    private GoogleMap googleMap;
    private ClusterManager<Passenger> MclusterManager;



    public PersonRenderer(Context context,GoogleMap googleMap) {

        super(context, googleMap, mClusterManager);
        this.context = context;
        this.googleMap = googleMap;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate( R.layout.multi_profile, null );

        mClusterIconGenerator.setContentView(multiProfile);
        mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

        mImageView = new ImageView(context);



        mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
        mImageView.setPadding(padding, padding, padding, padding);
        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(Person person, MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name.
        mImageView.setImageResource(person.profilePhoto);
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<Person> cluster, MarkerOptions markerOptions) {
        // Draw multiple people.
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
        List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
        int width = mDimension;
        int height = mDimension;

        for (Person p : cluster.getItems()) {
            // Draw 4 at most.
            if (profilePhotos.size() == 4) break;
            Drawable drawable = getResources().getDrawable(p.profilePhoto);
            drawable.setBounds(0, 0, width, height);
            profilePhotos.add(drawable);
        }
        MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
        multiDrawable.setBounds(0, 0, width, height);

        mClusterImageView.setImageDrawable(multiDrawable);
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}*/
